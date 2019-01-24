package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.Robot;
public class DriveTrain extends Subsystem
{
    WPI_TalonSRX[] motors = new WPI_TalonSRX[4];
    public DriveTrain()
    {
        motors[0] = RobotMap.lFMaster;
        motors[1] = RobotMap.lBMaster;
        motors[2] = RobotMap.rFMaster;
        motors[3] = RobotMap.rBMaster;
        config();
    }

	@Override
	protected void initDefaultCommand() {
		
    }
    


    private void config()
    {
        for(int i = 0; i < motors.length; i ++)
        {
       
            // motors[i].setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
            // motors[i].setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

            motors[i].configNominalOutputForward(0, Constants.kTimeoutMs);
            motors[i].configNominalOutputReverse(0, Constants.kTimeoutMs);
            motors[i].configPeakOutputForward(1, Constants.kTimeoutMs);
            motors[i].configPeakOutputReverse(-1, Constants.kTimeoutMs);

            motors[i].config_kF(Constants.kPIDLoopIdx, Constants.kF, Constants.kTimeoutMs);
            motors[i].config_kP(Constants.kPIDLoopIdx, Constants.kP, Constants.kTimeoutMs);
            motors[i].config_kI(Constants.kPIDLoopIdx, Constants.kI, Constants.kTimeoutMs);
            motors[i].config_kD(Constants.kPIDLoopIdx, Constants.kD, Constants.kTimeoutMs);
          
            /* Set acceleration and vcruise velocity - see documentation */
            // motors[i].configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
            // motors[i].configMotionAcceleration(6000, Constants.kTimeoutMs);

            /* Zero the sensor */
            motors[i].setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        }
    }

    public void setDistance(double distance, int motorID)
    {
        double targetPositionRotations =  distance;

        motors[motorID].set(ControlMode.Position, targetPositionRotations);

        while (motors[motorID].getControlMode() == ControlMode.Position) {
            System.out.println("MotorID: " + motorID );
            System.out.println(motors[motorID].getSelectedSensorPosition(0));
        }
    /*    for(int i = 0; i < motors.length; i ++)
        {
            int direction = 1;
            if (i >= 2)
                direction *= -1;
            motors[i].set(ControlMode.Position, targetPositionRotations * direction);
            

        } */
    }
    //Not Working
    public void setVelocity(double speed)
    {
        double targetVelocity_UnitsPer100ms = speed * 500.0 * 4096 / 600;
        for(int i = 0; i < motors.length; i ++)
        {
            System.out.println("PID Looping " + i);

            //Saw that Right side was reversed, 
            //will reverse them in robot map after testing complete
            int direction =1;
            if(i >=2)
                direction *=-1;

            System.out.println("Target Velocity: " + targetVelocity_UnitsPer100ms);
            motors[i].set(ControlMode.Velocity, targetVelocity_UnitsPer100ms *direction);
        }
    }

    public void motionMagic(double speed)
    {

        double targetPos = speed * 4096 * 10.0;
        for(int i = 0; i < motors.length; i ++)
            motors[i].set(ControlMode.MotionMagic, targetPos);
    }

    public void tankDrive()
    {
        move(-Robot.oi.j0.getY(), -Robot.oi.j1.getY());
    }

    public void move(double leftSpeed, double rightSpeed)
    {
        RobotMap.driveTank.tankDrive(leftSpeed, rightSpeed);
    }

    public void stop()
    {
        RobotMap.driveTank.stopMotor();
    }
}