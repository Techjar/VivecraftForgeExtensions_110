package com.techjar.vivecraftforge.util;

import net.minecraft.util.math.vector.Vector3d;

public class VRPlayerData {
	public ObjectInfo head = new ObjectInfo();
	public ObjectInfo controller0 = new ObjectInfo();
	public ObjectInfo controller1 = new ObjectInfo();
	public boolean handsReversed;
	public float worldScale;
	public boolean seated;
	public boolean freeMove;
	public float bowDraw;
	public float height;
	public int activeHand;

	public ObjectInfo getController(int c) {
		return c == 0 ? controller0 : controller1;
	}

	public static class ObjectInfo {
		public double posX;
		public double posY;
		public double posZ;
		public float rotW;
		public float rotX;
		public float rotY;
		public float rotZ;

		public Vector3d getPos() {
			return new Vector3d(posX, posY, posZ);
		}

		public void setPos(Vector3d pos) {
			posX = pos.getX();
			posY = pos.getY();
			posZ = pos.getZ();
		}

		public Quaternion getRot() {
			return new Quaternion(rotW, rotX, rotY, rotZ);
		}

		public void setRot(Quaternion quat) {
			rotW = quat.w;
			rotX = quat.x;
			rotY = quat.y;
			rotZ = quat.z;
		}
	}
}
