/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vkfriendsgraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class UserPainter {
    
    private Point2D centerPoint;
    private String firstName;
    private String lastName;
    private Image photo50;
    private User user;
    
    public UserPainter(Point2D centerPoint, User user) {
        this.centerPoint = centerPoint;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.photo50 = user.getPhoto50();
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
    
    public Point2D getCenter() {
        return centerPoint;
    }
    
    public void paint(Graphics2D g) {
        FontRenderContext context = g.getFontRenderContext();
        if (10 * (Properties.getScale() / 1.2) < 40)
            g.setFont(g.getFont().deriveFont((float)(10 * (Properties.getScale() / 1.2))));
        else 
            g.setFont(g.getFont().deriveFont((float)(40)));
        Font font = g.getFont();
        Rectangle2D fNameRect = font.getStringBounds(firstName, context);
        Rectangle2D lNameRect = font.getStringBounds(lastName, context);
        double fullWidth = (fNameRect.getWidth() > lNameRect.getWidth() ? fNameRect.getWidth() : lNameRect.getWidth());
        double fullHeight = fNameRect.getHeight() + lNameRect.getHeight();

        Ellipse2D ellipse = new Ellipse2D.Double(centerPoint.getX() - (fullWidth > fullHeight ? fullWidth / 2 : fullHeight / 2), 
                centerPoint.getY() - (fullWidth > fullHeight ? fullWidth / 2 : fullHeight / 2), 
                (fullWidth > fullHeight ? fullWidth : fullHeight), (fullWidth > fullHeight ? fullWidth : fullHeight));

        Color lastColor = g.getColor();
        Image img = photo50;
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr =  bi.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        g.setPaint(new TexturePaint(bi, ellipse.getBounds()));
        g.fill(ellipse);
        g.setColor(lastColor);
        g.draw(ellipse);

        g.drawString(firstName, (float)(centerPoint.getX() - fNameRect.getWidth() / 2), 
                (float)(centerPoint.getY() - fullHeight / 2 - fNameRect.getY()));
        g.drawString(lastName, (float)(centerPoint.getX() - lNameRect.getWidth() / 2), 
                (float)(centerPoint.getY() + fullHeight / 2 - lNameRect.getHeight() - lNameRect.getY()));
    }
}
