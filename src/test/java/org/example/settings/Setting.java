package org.example.settings;
import org.example.model.ElementInfo;
import com.google.gson.Gson;
import org.openqa.selenium.Proxy;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

    public class Setting {

        protected static WebDriver driver;
        protected static Actions actions;
        protected Logger logger = LoggerFactory.getLogger(getClass());
        DesiredCapabilities capabilities;
        ChromeOptions chromeOptions;
        FirefoxOptions firefoxOptions;



        private static final String DEFAULT_DIRECTORY_PATH = "elementValues";
        ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

        @BeforeScenario
        public void setUp() {
            logger.info("************************************  BeforeScenario  ************************************");

            driver = new ChromeDriver(chromeOptions());

            actions = new Actions(driver);
            driver.manage().timeouts().pageLoadTimeout(60L,TimeUnit.SECONDS);
        }

        @AfterScenario
        public void tearDown() {
            driver.quit();
        }

        public void initMap(File[] fileList) {
            Type elementType = new TypeToken<List<ElementInfo>>() {
            }.getType();
            Gson gson = new Gson();
            List<ElementInfo> elementInfoList = null;
            for (File file : fileList) {
                try {
                    elementInfoList = gson
                            .fromJson(new FileReader(file), elementType);
                    elementInfoList.parallelStream()
                            .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
                } catch (FileNotFoundException e) {
                    logger.warn("{} not found", e);
                }
            }
        }

        public File[] getFileList() {
            File[] fileList = new File(
                    this.getClass().getClassLoader().getResource(DEFAULT_DIRECTORY_PATH).getFile())
                    .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
            if (fileList == null) {
                logger.warn(
                        "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                        DEFAULT_DIRECTORY_PATH);
                throw new NullPointerException();
            }
            return fileList;
        }


        public ChromeOptions chromeOptions() {
            chromeOptions = new ChromeOptions();
            capabilities = DesiredCapabilities.chrome();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            chromeOptions.setExperimentalOption("prefs", prefs);
            chromeOptions.addArguments("--kiosk");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--start-fullscreen");
            System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver.exe");
            chromeOptions.merge(capabilities);
            return chromeOptions;
        }


        public ElementInfo findElementInfoByKey(String key) {
            return (ElementInfo) elementMapList.get(key);
        }

        public void saveValue(String key, String value) {
            elementMapList.put(key, value);
        }

        public String getValue(String key) {
            return elementMapList.get(key).toString();
        }



    }

