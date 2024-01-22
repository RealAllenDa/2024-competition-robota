package net.ironpulse.swerve;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.utility.PhoenixPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class FieldCentricTargetTx implements SwerveRequest {
    /**
     * The velocity in the X direction, in m/s.
     * X is defined as forward according to WPILib convention,
     * so this determines how fast to travel forward.
     */
    public double VelocityX = 0;

    public double CurrentTx = 0;
    /**
     * The angular rate to rotate at, in radians per second.
     * Angular rate is defined as counterclockwise positive,
     * so this determines how fast to turn counterclockwise.
     */
    public double RotationalRate = 0;
    /**
     * The allowable deadband of the request.
     */
    public double Deadband = 0;
    /**
     * The rotational deadband of the request.
     */
    public double RotationalDeadband = 0;
    /**
     * The type of control request to use for the drive motor.
     */
    public SwerveModule.DriveRequestType DriveRequestType = SwerveModule.DriveRequestType.OpenLoopVoltage;
    /**
     * The type of control request to use for the steer motor.
     */
    public SwerveModule.SteerRequestType SteerRequestType = SwerveModule.SteerRequestType.MotionMagic;
    public PhoenixPIDController TxController = new PhoenixPIDController(0, 0, 0);

    @Override
    public StatusCode apply(SwerveRequest.SwerveControlRequestParameters parameters, SwerveModule... modulesToApply) {
        double toApplyX = VelocityX;
        double toApplyY = -TxController.calculate(CurrentTx,
                0, parameters.timestamp);
        double toApplyOmega = RotationalRate;
        if (Math.sqrt(toApplyX * toApplyX + toApplyY * toApplyY) < Deadband) {
            toApplyX = 0;
            toApplyY = 0;
        }
        if (Math.abs(toApplyOmega) < RotationalDeadband) {
            toApplyOmega = 0;
        }

        ChassisSpeeds speeds = ChassisSpeeds.discretize(ChassisSpeeds.fromFieldRelativeSpeeds(toApplyX, toApplyY, toApplyOmega,
                parameters.currentPose.getRotation()), parameters.updatePeriod);

        var states = parameters.kinematics.toSwerveModuleStates(speeds, new Translation2d());

        for (int i = 0; i < modulesToApply.length; ++i) {
            modulesToApply[i].apply(states[i], DriveRequestType, SteerRequestType);
        }

        return StatusCode.OK;
    }

    /**
     * Sets the velocity in the X direction, in m/s.
     * X is defined as forward according to WPILib convention,
     * so this determines how fast to travel forward.
     *
     * @param velocityX Velocity in the X direction, in m/s
     * @return this request
     */
    public FieldCentricTargetTx withVelocityX(double velocityX) {
        this.VelocityX = velocityX;
        return this;
    }

    /**
     * Sets the desired direction to face.
     * 0 Degrees is defined as in the direction of the X axis.
     * As a result, a TargetDirection of 90 degrees will point along
     * the Y axis, or to the left.
     *
     * @param currentTx Desired direction to face
     * @return this request
     */
    public FieldCentricTargetTx withTargetTx(double currentTx) {
        this.CurrentTx = currentTx;
        return this;
    }

    /**
     * Sets the allowable deadband of the request.
     *
     * @param deadband Allowable deadband of the request
     * @return this request
     */
    public FieldCentricTargetTx withDeadband(double deadband) {
        this.Deadband = deadband;
        return this;
    }
    /**
     * Sets the rotational deadband of the request.
     *
     * @param rotationalDeadband Rotational deadband of the request
     * @return this request
     */
    public FieldCentricTargetTx withRotationalDeadband(double rotationalDeadband) {
        this.RotationalDeadband = rotationalDeadband;
        return this;
    }

    /**
     * Sets the type of control request to use for the drive motor.
     *
     * @param driveRequestType The type of control request to use for the drive motor
     * @return this request
     */
    public FieldCentricTargetTx withDriveRequestType(SwerveModule.DriveRequestType driveRequestType) {
        this.DriveRequestType = driveRequestType;
        return this;
    }
    /**
     * Sets the type of control request to use for the steer motor.
     *
     * @param steerRequestType The type of control request to use for the steer motor
     * @return this request
     */
    public FieldCentricTargetTx withSteerRequestType(SwerveModule.SteerRequestType steerRequestType) {
        this.SteerRequestType = steerRequestType;
        return this;
    }

    /**
     * The angular rate to rotate at, in radians per second.
     * Angular rate is defined as counterclockwise positive,
     * so this determines how fast to turn counterclockwise.
     *
     * @param rotationalRate Angular rate to rotate at, in radians per second
     * @return this request
     */
    public FieldCentricTargetTx withRotationalRate(double rotationalRate) {
        this.RotationalRate = rotationalRate;
        return this;
    }
}
