package com.deskapp.demo;

/** File: ControlPanel.java
 *俄罗斯方块游戏的控制部分的实现 */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
class ControlPanel extends JPanel {

    private JTextField tfLevel = new JTextField("" + ErsBlocksGame.DEFAULT_LEVEL);
	//用一个文本域显示难度级别
	private JTextField
    //用一个文本域显示玩家得分
	tfScore = new JTextField("0");
    	private JButton           //声明一组控制按钮
	        btPlay = new JButton("开始"),
	        btPause = new JButton("暂停"),
	        btStop = new JButton("停止"),
	        btTurnLevelUp = new JButton("增加难度"),
	        btTurnLevelDown = new JButton("降低难度");
		//提示下一个游戏块的面板
	private JPanel plTip = new JPanel(new BorderLayout());
	private TipPanel plTipBlock = new TipPanel();
	 	//显示游戏当前信息的面板，4行1列
	private JPanel plInfo = new JPanel(new GridLayout(4, 1));
        	//存放控制按钮的面板，5行1列
	private JPanel plButton = new JPanel(new GridLayout(5, 1));
   	private Timer timer;
		    //当前的游戏局
	private ErsBlocksGame game;
     	//设置突出的EtchedBorder类型的边框
	private Border border = new EtchedBorder(
	        EtchedBorder.RAISED, Color.WHITE, new Color(148, 145, 140));
	     //主控制面板类的构造函数
	public ControlPanel(final ErsBlocksGame game) {
		  //整个控制面板有3个子面板，摆放在1列，每行的间距为4
		setLayout(new GridLayout(3, 1, 0, 4));
		this.game = game;
	      //预提示面板的两个构件及边界
		plTip.add(new JLabel("下一个方块"), BorderLayout.NORTH);
		plTip.add(plTipBlock);
		plTip.setBorder(border);
	        //游戏信息显示面板的两个标签和两个文本域及边界
		plInfo.add(new JLabel("难度级别"));
		plInfo.add(tfLevel);
		plInfo.add(new JLabel("得分"));
		plInfo.add(tfScore);
		plInfo.setBorder(border);
		//两个文本域都是不可编辑的，只用于显示信息
		tfLevel.setEditable(false);
		tfScore.setEditable(false);
		//按钮面板的五个按钮及边界
		plButton.add(btPlay);
		plButton.add(btPause);
		plButton.add(btStop);
		plButton.add(btTurnLevelUp);
		plButton.add(btTurnLevelDown);
		plButton.setBorder(border);
	     	//将3个合成面板加入到主面板中
		add(plTip);
		add(plInfo);
		add(plButton);
			//增加键盘的监听器		
		addKeyListener(new ControlKeyListener());
		    //增加按钮的监听器
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				game.playGame();
			}
		});
		btPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (btPause.getText().equals(new String("暂停"))) {
					game.pauseGame();
				} else {
					game.resumeGame();
				}
			}
		});
		btStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				game.stopGame();
			}
		});
		btTurnLevelUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int level = Integer.parseInt(tfLevel.getText());
					if (level < ErsBlocksGame.MAX_LEVEL)
						tfLevel.setText("" + (level + 1));
				} catch (NumberFormatException e) {
				}
				requestFocus();
			}
		});
		btTurnLevelDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int level = Integer.parseInt(tfLevel.getText());
					if (level > 1)
						tfLevel.setText("" + (level - 1));
				} catch (NumberFormatException e) {
				}
				//requestFocus();
			}
		});
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				plTipBlock.fanning();
			}
		});
		timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//显示玩家的得分
				tfScore.setText("" + game.getScore());
				//按得分晋升难度级别
				int scoreForLevelUpdate =
				        game.getScoreForLevelUpdate();
	            //显示更新后的难度级别
				if (scoreForLevelUpdate >= ErsBlocksGame.PER_LEVEL_SCORE
				        && scoreForLevelUpdate > 0)
					game.levelUpdate();
			}
		});
		timer.start();//启动定时器
	}
	    //设置预显窗口的样式，
	public void setTipStyle(int style) {
		plTipBlock.setStyle(style);
	}
	    //取得用户设置的游戏等级。
	public int getLevel() {
		int level = 0;
		try {
			level = Integer.parseInt(tfLevel.getText());
		} catch (NumberFormatException e) {	}
		return level;
	}
	    //让用户修改游戏难度等级。
	public void setLevel(int level) {
		if (level > 0 && level < 11) tfLevel.setText("" + level);
	}
		//设置"开始"按钮的状态。
	public void setPlayButtonEnable(boolean enable) {
		btPlay.setEnabled(enable);
	}
	    //根据pause的值设置按钮的显示标签
	public void setPauseButtonLabel(boolean pause) {
		btPause.setText(pause ? "暂停" : "继续");
	}
		// 重置控制面板，得分置为0
	public void reset() {
		tfScore.setText("0");
		plTipBlock.setStyle(0);
	}
		//重新计算TipPanel里的boxes[][]里的方块的大小
	public void fanning() {
		plTipBlock.fanning();
	}
		// 提示信息面板的实现细节类
	private class TipPanel extends JPanel {
		//提示信息面板的前景和背景颜色
		private Color backColor = Color.LIGHT_GRAY, frontColor = Color.red;
		//创建设定的行数和列数的俄罗斯方块
		private ErsBox[][] boxes =
		        new ErsBox[ErsBlock.BOXES_ROWS][ErsBlock.BOXES_COLS];
        //块的形态、宽度和高度
		private int style, boxWidth, boxHeight;
			//isTiled是否平铺
		private boolean isTiled = false;
	//预提示窗口类构造函数
		public TipPanel() {
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++)
					boxes[i][j] = new ErsBox(false);
			}
		}
		public TipPanel(Color backColor, Color frontColor) {
			this();
			this.backColor = backColor;
			this.frontColor = frontColor;
		}
			//设置预显窗口的方块样式
		public void setStyle(int style) {
			this.style = style;
			repaint();
		}
		// 覆盖JComponent类的函数，画组件。
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isTiled) fanning();
			int key = 0x8000;
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					Color color = (((key & style) != 0) ? frontColor : backColor);
					g.setColor(color);
					g.fill3DRect(j * boxWidth, i * boxHeight,
					        boxWidth, boxHeight, true);
					key >>= 1;
				}
			}
		}
		// 根据窗口的大小，自动调整方格的大小
		public void fanning() {
			boxWidth = getSize().width / ErsBlock.BOXES_COLS;
			boxHeight = getSize().height / ErsBlock.BOXES_ROWS;
			isTiled = true;
		}
	}
		//游戏的按钮控制键的监听器
	private class ControlKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			if (!game.isPlaying()) return;
			ErsBlock block = game.getCurBlock();
			switch (ke.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					block.moveDown();
					break;
				case KeyEvent.VK_LEFT:
					block.moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					block.moveRight();
					break;
				case KeyEvent.VK_UP:
					block.turnNext();
					break;
				default:
					break;
			}
		}
	}
}
