import java.util.Scanner;
import javax.swing.*;
import java.util.List;

public class MyProgram
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlightPlannerSwing planner = new FlightPlannerSwing();
                planner.setVisible(true);
            }
        });
    }
    
}
