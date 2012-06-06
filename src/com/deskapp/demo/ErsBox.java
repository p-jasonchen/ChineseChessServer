package com.deskapp.demo;

/** File: ErsBox.java
 *俄罗斯方格的 Java 实现 */
import java.awt.*;
class ErsBox implements Cloneable {
	private boolean isColor;
	private Dimension size = new Dimension();
	public ErsBox(boolean isColor) {
		this.isColor = isColor;
	}
	    //此方格是不是用前景色表现
	public boolean isColorBox() {
		return isColor;
	}
	    // 设置方格的颜色，
	public void setColor(boolean isColor) {
		this.isColor = isColor;
	}
	//得到此方格的尺寸
	public Dimension getSize() {
		return size;
	}
	//设置方格的尺寸
	public void setSize(Dimension size) {
		this.size = size;
	}
	public Object clone() {
		Object cloned = null;
		try { cloned = super.clone();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cloned;
	}
}
