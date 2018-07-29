package werewolvesAndVampires.core;

import org.lwjgl.util.vector.Quaternion;

import static java.lang.Math.*;

public class WVMath {

	/**
	 * Same as mentioned method but angles are in degrees
	 * @see WVMath#eulerToQuaternion(double, double, double)
	 */
	public static Quaternion eulerToQuaternionDegrees(double x, double y, double z) {
		return eulerToQuaternion(toRadians(x), toRadians(y), toRadians(z));
	}

	/**
	 * Converts euler angles to {@link org.lwjgl.util.vector.Quaternion}.
	 * Angles are in radians
	 * @param x X angle
	 * @param y Y angle
	 * @param z Z angle
	 * @return a Quaternion
	 */
	public static Quaternion eulerToQuaternion(double x, double y, double z) {

		double c1 = cos(x / 2);
		double c2 = cos(y / 2);
		double c3 = cos(z / 2);

		double s1 = sin(x / 2);
		double s2 = sin(y / 2);
		double s3 = sin(z / 2);

		double w = sqrt(1.0 + c1 * c2 + c1 * c3 - s1 * s2 * s3 + c2 * c3) / 2;
		double quatX = (c2  * s3 + c1 * s3 + s1 * s2 * c3)                / (4.0 * w);
		double quatY = (s1  * c2 + s1 * c3 + c1 * s2 * s3)                / (4.0 * w);
		double quatZ = (-s1 * s3 + c1 * s2 * c3 + s2)                     / (4.0 * w);

		return new Quaternion(
				(float) quatX,
				(float) quatY,
				(float) quatZ,
				(float) w
		);
	}

}
