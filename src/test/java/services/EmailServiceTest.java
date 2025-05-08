package services;

import static org.junit.Assert.*;
import org.junit.Test;


public class EmailServiceTest {

    
    @Test
    public void testSendEmail() {
        
        try {
            
            boolean result = EmailService.sendEmail("test@example.com", "Test Subject", "Test Body");

            
            assertTrue("Email sending should always return true", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    
    @Test
    public void testSendEmailWithInvalidAddress() {
        
        try {
            
            boolean result = EmailService.sendEmail("invalid-email", "Test Subject", "Test Body");

            
            assertTrue("Email sending should return true even with invalid email", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    
    @Test
    public void testSendEmailWithNullAddress() {
        
        try {
            
            boolean result = EmailService.sendEmail(null, "Test Subject", "Test Body");

            
            assertTrue("Email sending should return true even with null email", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
