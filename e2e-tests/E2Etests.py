# e2e_onlybuns_simple.py
import os
import time
import pytest

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.webdriver import WebDriver
from selenium.webdriver.support.ui import Select
import chromedriver_autoinstaller


"""
Jedan browser fixture za sve testove.
Pokreće lokalni frontend na http://localhost:3000
Kredencijale i URL možeš prepisati iz env varijabli.
"""

APPLICATION_URL = "http://localhost:3000"
USER_A = "relja"
USER_A_PW = "123"
USER_B_ID =  "1"


@pytest.fixture()
def browser():
    chromedriver_autoinstaller.install()
    driver = webdriver.Chrome()
    driver.implicitly_wait(10)
    yield driver
    driver.quit()


class TestE2e:
    # 1) Login: happy path
    def test_login_success(self, browser: 'WebDriver'):
        browser.get(f"{APPLICATION_URL}/login")

        username_input = browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[1]/input")
        password_input = browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[2]/input")
        login_button = browser.find_element(By.XPATH, "//button[text()='Login']")

        username_input.send_keys(USER_A)
        password_input.send_keys(USER_A_PW)
        login_button.click()

        # nešto vidljivo posle login-a (avatar ili naslov)
        profile_button = browser.find_element(By.XPATH, "//a[text()='Profile']")
        assert profile_button.is_displayed()

    # 2) Login: invalid credentials
    def test_login_invalid_credentials(self, browser: 'WebDriver'):
        browser.get(f"{APPLICATION_URL}/login")

        username_input = browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[1]/input")
        password_input = browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[2]/input")
        login_button = browser.find_element(By.XPATH, "//button[text()='Login']")

        username_input.send_keys(USER_A)
        password_input.send_keys(USER_A_PW + "x")  # pogrešna lozinka
        login_button.click()

        error_msg = browser.find_element(
            By.XPATH, "//p[text()='Invalid username or password']"
        )
        assert error_msg.is_displayed()


    # 3) Follow/Unfollow flow 
    def test_follow_unfollow_flow(self, browser: 'WebDriver'):
        # login
        browser.get(f"{APPLICATION_URL}/login")
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[1]/input").send_keys(USER_A)
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[2]/input").send_keys(USER_A_PW)
        browser.find_element(By.XPATH, "//button[text()='Login']").click()
        browser.find_element(By.XPATH, "//a[text()='Profile']")  # anchor posle logina

        # idemo na profil target korisnika
        browser.get(f"{APPLICATION_URL}/profile/{USER_B_ID}")

        # pokušaj Follow (ako već prati, prvo Unfollow)
        try:
            unfollow_btn = browser.find_element(By.XPATH, "//button[text()='Unfollow']")
            unfollow_btn.click()
            
            time.sleep(1)
        except:
            pass

        follow_btn = browser.find_element(By.XPATH, "//button[text()='Follow']")
        follow_btn.click()
        time.sleep(1)
        unfollow_btn = browser.find_element(By.XPATH, "//button[text()='Unfollow']")
        assert unfollow_btn.is_displayed()

        # vrati stanje 
        unfollow_btn.click()
        time.sleep(1)
        follow_btn = browser.find_element(By.XPATH, "//button[text()='Follow' or text()='Prati']")
        assert follow_btn.is_displayed()

    # 4) Update password (happy path) + re-login sa novom lozinkom
    def test_update_password_happy_path(self, browser: 'WebDriver'):
        # login
        browser.get(f"{APPLICATION_URL}/login")
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[1]/input").send_keys(USER_A)
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[2]/input").send_keys(USER_A_PW)
        browser.find_element(By.XPATH, "//button[text()='Login']").click()

        browser.find_element(By.XPATH, "//a[text()='Profile']").click()  

        # idi na settings 
        settings_icon = browser.find_element(By.CSS_SELECTOR, "i.fas.fa-cog")
        settings_icon.click()

        # izaberi Edit Password iz dropdown menija
        browser.find_element(By.XPATH, "//div[text()='Edit Password']").click()


        current_pw_input = browser.find_element(By.XPATH, "//input[@placeholder='Current Password']")
        new_pw_input = browser.find_element(By.XPATH, "//input[@placeholder='New Password']")
        confirm_pw_input = browser.find_element(By.XPATH, "//input[@placeholder='Confirm Password']")
        save_pw_btn = browser.find_element(By.XPATH, "//button[text()='Save']")

        # promeni lozinku 
        NEW_PW = USER_A_PW + "x!"
        current_pw_input.clear(); 
        current_pw_input.send_keys(USER_A_PW)
        new_pw_input.clear(); 
        new_pw_input.send_keys(NEW_PW)
        confirm_pw_input.send_keys(NEW_PW)
        save_pw_btn.click()


        time.sleep(1)
        # potvrda 
        alert = browser.switch_to.alert  
        assert "Password updated successfully!" in alert.text  
        alert.accept()

   
        browser.get(f"{APPLICATION_URL}/login")
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[1]/input").send_keys(USER_A)
        browser.find_element(By.XPATH, "//*[@id='root']/div/div[2]/div/div/form/div[2]/input").send_keys(NEW_PW)
        browser.find_element(By.XPATH, "//button[text()='Login']").click()

        profile_button = browser.find_element(By.XPATH, "//a[text()='Profile']")
        assert profile_button.is_displayed()
