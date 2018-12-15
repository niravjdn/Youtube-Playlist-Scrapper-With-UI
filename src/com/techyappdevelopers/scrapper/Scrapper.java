package com.techyappdevelopers.scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.mike10004.xvfbmanager.XvfbController;
import com.github.mike10004.xvfbmanager.XvfbManager;
import com.google.common.collect.ImmutableMap;

/**
 * Hello world!
 *
 */
public class Scrapper {
	static String url;
	private static WebDriver driver;
	public static List<String> playlistNameList = new ArrayList<String>();
	public static List<String> playlistIDList = new ArrayList<String>();

	public void scrape(String urlOfPlaylist, String driverPath) throws IOException {
		playlistIDList.clear();
		playlistNameList.clear();
		Scrapper.url = urlOfPlaylist;
		String exePath;
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			driverPath = "C:/driver/chromedriver.exe";
			exePath = "chromedriver.exe";
		} else if (isUnix(OS)) {
			driverPath = "/usr/bin/";
			exePath = "chromedriver";
		} else {
			driverPath = "/Workspace/Eclipse/Youtube Playlist Scrapper/WebContent/WEB-INF/lib/Driver/";
			exePath = "chromedriver";
		}
		System.out.println(driverPath);

		XvfbManager manager = new XvfbManager();
		XvfbController display = manager.start(1);

		System.setProperty("webdriver.chrome.driver", driverPath + exePath);

		ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
				.withEnvironment(ImmutableMap.of("DISPLAY", ":1")).build();
		chromeDriverService.start();

		ChromeOptions options = new ChromeOptions();

		// "--headless"
		options.addArguments("--disable-gpu", "--window-size=1024,768", "--ignore-certificate-errors", "--silent");

		driver = new ChromeDriver(chromeDriverService, options);

		try {
			driver.get(url);

			falseWait();

			try {
				for (int i = 0; i < 10; i++) {
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 5000);");
					Thread.sleep(2000);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			List<WebElement> anchors = driver.findElements(By.tagName("a"));
			for (WebElement anchor : anchors) {
				try {
					String href = anchor.getAttribute("href");
					String spellcheck = anchor.getAttribute("spellcheck");
					System.out.println(href);
					if (spellcheck.equals("false") && href.contains("list=") && href.contains("/watch?v=")) {

						String item = anchor.getAttribute("href");
						int startIndex = item.indexOf("list=") + 5;
						String playlistID = item.substring(startIndex);
						if (playlistID.startsWith("LL")) {
							continue;
						}
						String playlistName = anchor.getText();
						playlistNameList.add("» " + playlistName);
						playlistIDList.add(playlistID);
						System.out.println(playlistID);
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}

//				FileWriter writer = new FileWriter("output.txt");
//				writer.write("\"");
//				for (String str : playlistIDList) {
//					writer.write(str);
//				}
//				writer.write("\"");
//				writer.close();
//
//				writer = new FileWriter("playlistName.txt");
//				for (String str : playlistNameList) {
//					writer.write(str + "\n");
//				}
//				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
	}

	public static void falseWait() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dummy")));
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static boolean isUnix(String OS) {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

}
