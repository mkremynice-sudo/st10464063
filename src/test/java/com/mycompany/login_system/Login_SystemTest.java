package com.mycompany.login_system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Login_SystemTest {

    // ================= USERNAME TESTS =================

    @Test
    void testValidUsername() {

        assertTrue(
                Login_System.checkUserName("ab_cd")
        );
    }

    @Test
    void testInvalidUsername_NoUnderscore() {

        assertFalse(
                Login_System.checkUserName("abcde")
        );
    }

    @Test
    void testInvalidUsername_TooLong() {

        assertFalse(
                Login_System.checkUserName("abc_def")
        );
    }

    // ================= PASSWORD TESTS =================

    @Test
    void testValidPassword() {

        assertTrue(
                Login_System.checkPassword("Password1!")
        );
    }

    @Test
    void testInvalidPassword_NoCapital() {

        assertFalse(
                Login_System.checkPassword("password1!")
        );
    }

    @Test
    void testInvalidPassword_NoNumber() {

        assertFalse(
                Login_System.checkPassword("Password!")
        );
    }

    @Test
    void testInvalidPassword_NoSpecialCharacter() {

        assertFalse(
                Login_System.checkPassword("Password1")
        );
    }

    @Test
    void testInvalidPassword_TooShort() {

        assertFalse(
                Login_System.checkPassword("P1!a")
        );
    }

    // ================= PHONE NUMBER TESTS =================

    @Test
    void testValidPhoneNumber() {

        assertTrue(
                Login_System.checkPhone("+27123456789")
        );
    }

    @Test
    void testInvalidPhoneNumber() {

        assertFalse(
                Login_System.checkPhone("0712345678")
        );
    }

    // ================= MESSAGE TESTS =================

    @Test
    void testMessageIDNotNull() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Hello world"
                );

        assertNotNull(msg.getMessageID());
    }

    @Test
    void testMessageHashNotNull() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Hello world"
                );

        assertNotNull(msg.getMessageHash());
    }

    @Test
    void testMessageHashContainsWords() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Hello Chat"
                );

        assertTrue(
                msg.getMessageHash().contains("HELLOCHAT")
        );
    }

    @Test
    void testRecipientStoredCorrectly() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Testing"
                );

        assertEquals(
                "+27123456789",
                msg.getRecipient()
        );
    }

    @Test
    void testMessageStoredCorrectly() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Testing message"
                );

        assertEquals(
                "Testing message",
                msg.getMessage()
        );
    }

    @Test
    void testMarkMessageAsSent() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Test"
                );

        msg.markAsSent();

        assertTrue(msg.isSent());
    }

    // ================= EDGE CASE TESTS =================

    @Test
    void testSingleWordMessageHash() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        "Hello"
                );

        assertNotNull(msg.getMessageHash());
    }

    @Test
    void testEmptyMessage() {

        Login_System.Message msg =
                new Login_System.Message(
                        1,
                        "+27123456789",
                        ""
                );

        assertNotNull(msg.getMessageHash());
    }
}