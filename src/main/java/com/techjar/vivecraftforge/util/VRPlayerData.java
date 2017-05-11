package com.techjar.vivecraftforge.util;

public class VRPlayerData {
	public ObjectInfo head = new ObjectInfo();
	public ObjectInfo controller0 = new ObjectInfo();
	public ObjectInfo controller1 = new ObjectInfo();
	public boolean handsReversed;
	public float worldScale;
	public boolean seated;
	public boolean freeMove;
	public float bowDraw;

	public static class ObjectInfo {
		public float posX;
		public float posY;
		public float posZ;
		public float rotW;
		public float rotX;
		public float rotY;
		public float rotZ;
	}
}
