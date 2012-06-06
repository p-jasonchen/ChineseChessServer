package com.deskapp.demo;

/** File: GameCanvas.java
*俄罗斯方块的每一个方块的绘制 */
import javax.swing.*;
import javax.swing.border.EtchedBorder;
         //EtchedBorder为swing包中的突出或凹进的边框
import java.awt.*;
class GameCanvas extends JPanel {
//默认的方块的颜色为桔黄色，背景颜色为黑色
private Color backColor = Color.black, frontColor = Color.orange;
private int rows, cols, score = 0, scoreForLevelUpdate = 0;
private ErsBox[][] boxes;
private int boxWidth, boxHeight;
	//score：得分，scoreForLevelUpdate：上一次升级后的积分
// 画布类的第一个版本的构造函数
public GameCanvas(int rows, int cols) {
	this.rows = rows;
	this.cols = cols;
	//初始化rows*cols个ErsBox对象
	boxes = new ErsBox[rows][cols];
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++) {
			boxes[i][j] = new ErsBox(false);
		}
	}
        //设置画布的边界
	setBorder(new EtchedBorder(
	        EtchedBorder.RAISED, Color.white, new Color(148, 145, 140)));
}
	//画布类的第二个版本的构造函数
public GameCanvas(int rows, int cols, Color backColor, Color frontColor) {
	this(rows, cols);     //调用第一个版本的构造函数
	this.backColor = backColor;
	this.frontColor = frontColor;  //通过参数设置背景和前景颜色
}
	//设置游戏背景色彩
public void setBackgroundColor(Color backColor) {
	this.backColor = backColor;
}
	//取得游戏背景色彩
public Color getBackgroundColor() {
	return backColor;
}
	//设置游戏方块色彩
public void setBlockColor(Color frontColor) {
	this.frontColor = frontColor;
}
	//取得游戏方块色彩
public Color getBlockColor() {
	return frontColor;
}
    //取得画布中方格的行数
public int getRows() {
	return rows;
}
	//取得画布中方格的列数
public int getCols() {
	return cols;
}
	//取得游戏成绩
public int getScore() {
	return score;
}
    //取得自上一次升级后的积分
public int getScoreForLevelUpdate() {
	return scoreForLevelUpdate;
}
	//升级后，将上一次升级以来的积分清0
public void resetScoreForLevelUpdate() {
	scoreForLevelUpdate -= ErsBlocksGame.PER_LEVEL_SCORE;
}
	//得到某一行某一列的方格引用。
public ErsBox getBox(int row, int col) {
	if (row < 0 || row > boxes.length - 1
	        || col < 0 || col > boxes[0].length - 1)
		return null;
	return (boxes[row][col]);
}
     //覆盖JComponent类的函数，画组件。
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(frontColor);
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++) {
            //用前景颜色或背景颜色绘制每个方块
			g.setColor(boxes[i][j].isColorBox() ? frontColor : backColor);
			g.fill3DRect(j * boxWidth, i * boxHeight,
			        boxWidth, boxHeight, true);
		}
	}
}
    //根据窗口的大小，自动调整方格的尺寸
public void fanning() {
	boxWidth = getSize().width / cols;
	boxHeight = getSize().height / rows;
}
	//当一行被游戏者叠满后，将此行清除，并为游戏者加分
public synchronized void removeLine(int row) {
	for (int i = row; i > 0; i--) {
		for (int j = 0; j < cols; j++)
			boxes[i][j] = (ErsBox) boxes[i - 1][j].clone();
	}
	score += ErsBlocksGame.PER_LINE_SCORE;
	scoreForLevelUpdate += ErsBlocksGame.PER_LINE_SCORE;
	repaint();
}
	//重置画布，置积分为0
public void reset() {
	score = 0;
	scoreForLevelUpdate = 0;
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++)
			boxes[i][j].setColor(false);
	}
	repaint();
}
}