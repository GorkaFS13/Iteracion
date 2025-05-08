package domain;

import javax.swing.ImageIcon;
import java.io.Serializable;
import java.net.URL;


public enum CarType implements Serializable {
    SEDAN("Tractor", "tractor.png"),
    SUV("SUV", "SUV.png"),
    HATCHBACK("Hatchback", "hatchback.png"),
    MINIVAN("Minivan", "minivan.png"),
    LUXURY("Luxury", "luxury.png");

    private final String displayName;
    private final String imageName;

    CarType(String displayName, String imageName) {
        this.displayName = displayName;
        this.imageName = imageName;
    }

    
    public String getDisplayName() {
        return displayName;
    }

    
    public String getImageName() {
        return imageName;
    }

    
    public ImageIcon getImageIcon() {
        try {
            URL imageUrl = getClass().getClassLoader().getResource("images/cars/" + imageName);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            }
        } catch (Exception e) {
            System.err.println("Error loading car image: " + e.getMessage());
        }
        return null;
    }

    
    public ImageIcon getScaledImageIcon(int width, int height) {
        ImageIcon icon = getImageIcon();
        if (icon != null) {
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
        }
        return null;
    }

    
    public static CarType fromDisplayName(String displayName) {
        for (CarType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return SEDAN; 
    }

    @Override
    public String toString() {
        return displayName;
    }
}
