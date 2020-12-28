package Appium.AppiumPOM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseDriver {
	
	public String projectPath = System.getProperty("user.dir");
	String deviceName = configProperties().getProperty("Device_Name");
	String url=configProperties().getProperty("url");
	String appPackage = configProperties().getProperty("AppPackage");
	String appActivity = configProperties().getProperty("AppActivity");
	String androidVersion = configProperties().getProperty("PlatformVersion");
	AndroidDriver<AndroidElement> driver;
	
	@BeforeMethod
	public AndroidDriver<AndroidElement> LaunchApp() {
		// TODO Auto-generated method stub
		LaunchAppiumServer().start();
		DesiredCapabilities capabilities= new DesiredCapabilities();
		
		if(deviceName.contains("Emulator")) {
			
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
			
		}
		
		else if(deviceName.contains("Device")) {
			
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		}
		
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, androidVersion);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
		//capabilities.setCapability(MobileCapabilityType.APP, "");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		
		try {
			driver = new AndroidDriver<AndroidElement>(new URL(url), capabilities);
		} catch (MalformedURLException e) {
			System.out.println("Exception occured while initializing driver");
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return driver;
	}
	
	@AfterMethod
	public void tearDown() {
		
		driver.quit();
		LaunchAppiumServer().stop();
	}
	
	public AppiumDriverLocalService LaunchAppiumServer() {
		
		AppiumDriverLocalService service =  AppiumDriverLocalService
				.buildService(new AppiumServiceBuilder().usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
				.withAppiumJS(new File("C:\\Users\\mukha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")));
	
		return service;
	}
	
	public Properties configProperties() {
		
		FileInputStream fis = null;
		Properties configObkect = null;
	try {
		fis = new FileInputStream(projectPath+"\\src\\test\\java\\Resources\\config.properties");
	} catch (FileNotFoundException e) {
		System.out.println("Exception received while accessing config prperties file");
		e.printStackTrace();
		
		configObkect = new Properties();
		try {
			configObkect.load(fis);
		} catch (IOException e1) {
			System.out.println("Exception occured while loading properties from config properties");
			e1.printStackTrace();
		}
		
	}
	return configObkect;
	
	}

}
