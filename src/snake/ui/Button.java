package snake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;

import snake.Maths;
import snake.SnakeGame;
import snake.io.ImageLoader;
import snake.io.IniAdapter;
import snake.io.LangAdapter;
import snake.ui.listeners.MouseListener;


/**
 * 
 * @author Cedric
 * @version 1.0
 * @category ui
 *
 */
@SuppressWarnings("serial")
public class Button extends JComponent {

	private int id;
	private static int lastId;
	
	public enum buttonStates {
		RELEASED(0),
		FOCUSSED(1),
		PRESSED(2);
		
		public final int id;
		
		private buttonStates(int id) {
			this.id = id;
		}
	}
	
	private Image backgroundImage[];
	private Image lastImage;
	private BufferedImage lastImageBuffered;
	private Color fReleasedC, fFocussedC, fPressedC, bReleasedC, bFocussedC, bPressedC, fontColor;
	private String label;
	private Font font;
	
	private int borderW, borderH;
	private int buttonState;
	private int layerIndex = -1;
	
	private boolean resize = true;
	
	private static IniAdapter ini;
	private static int minimumFontSize;
	private static int default_borderW, default_borderH, minimumBorderW, minimumBorderH;
	private static int minTextMarginW, minTextMarginH;
	private static Color default_fReleasedC, default_fFocussedC, default_fPressedC, default_bReleasedC, default_bFocussedC, default_bPressedC, default_fontColorF;
	private static Font default_font;
	
	static {
		lastId=-1;
		ini = new IniAdapter();
		default_borderW = (int) Maths.format(ini.getString(SnakeGame.buttonIniPath, "default_borderW").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width));
		default_borderH = (int) Maths.format(ini.getString(SnakeGame.buttonIniPath, "default_borderH").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height));
		default_fReleasedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_fReleasedC"), 16));
		default_fFocussedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_fFocussedC"), 16));
		default_fPressedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_fPressedC"), 16));
		default_bReleasedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_bReleasedC"), 16));
		default_bFocussedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_bFocussedC"), 16));
		default_bPressedC = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_bPressedC"), 16));
		default_fontColorF = new Color(Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_fontColorF"), 16));
		String l_font_name = ini.getString(SnakeGame.buttonIniPath, "default_fontName");
		int l_font_style = Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "default_fontStyle").replaceAll("NONE", ""+Font.PLAIN).replaceAll("italic/bold", ""+Font.ITALIC+Font.BOLD).replaceAll("italic", ""+Font.BOLD).replaceAll("bold", ""+Font.BOLD));
		int l_font_size = (int) Maths.format(ini.getString(SnakeGame.buttonIniPath, "default_fontSize").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().getWidth()));
		minimumFontSize = Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "minimumFontSize"));
		minimumBorderW = Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "minimumBorderW"));
		minimumBorderH = Integer.parseInt(ini.getString(SnakeGame.buttonIniPath, "minimumBorderH"));
		minTextMarginW = (int) Maths.format(ini.getString(SnakeGame.buttonIniPath, "minimumTextMarginW").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().getWidth()));
		minTextMarginH = (int) Maths.format(ini.getString(SnakeGame.buttonIniPath, "minimumTextMarginH").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		l_font_size = l_font_size < minimumFontSize ? minimumFontSize : l_font_size;
		default_font = new Font(l_font_name, l_font_style, l_font_size);
		default_borderW = default_borderW < minimumBorderW ? minimumBorderW : default_borderW;
		default_borderH = default_borderH < minimumBorderH ? minimumBorderH : default_borderH;
		if (Boolean.parseBoolean(ini.getString(SnakeGame.buttonIniPath, "sameBorder"))) {
			if (default_borderW != default_borderH) default_borderW = default_borderH;
		}
		if (Boolean.parseBoolean(ini.getString(SnakeGame.buttonIniPath, "sameMinimumTextMargin"))) {
			if (minTextMarginW != minTextMarginH) minTextMarginW = minTextMarginH;
		}
	}
	
	// Constructors
	public Button() {
		this("");
	}
	
	public Button(String label) {
		this(label, default_font);
	}
	
	// all constructors go to this constructor
	public Button(String label, Font font) {
		this.label = label;
		this.font = font;
		fReleasedC = default_fReleasedC;
		fFocussedC = default_fFocussedC;
		fPressedC = default_fPressedC;
		bReleasedC = default_bReleasedC;
		bFocussedC = default_bFocussedC;
		bPressedC = default_bPressedC;
		fontColor = default_fontColorF;
		borderW = default_borderW;
		borderH = default_borderH;
		buttonState = buttonStates.RELEASED.id;
		id = ++lastId;
		JLabel l = new JLabel(label);
		l.setFont(font);
		setPreferredSize(new Dimension((int)(l.getPreferredSize().getWidth()+2*borderW+2*minTextMarginW), (int)l.getPreferredSize().getHeight()+2*borderH+2*minTextMarginH));
		// Listener here
		MouseListener.addButton(this);
	}
	
	/**
	 *  Creates a button from the ini at iniPath with the given component_name and returns it
	 *  @param iniPath: This is the path from which the button should be loaded
	 *  @param component_name: This is the name of the component. This should also be the start of every attribute in the ini
	 *  @return returns a button loaded from the ini and the component_name
	 */
	public static Button loadButtonFromIni(String iniPath, String component_name) {
		IniAdapter ini = new IniAdapter();
		String text;
		Button b = new Button((text=LangAdapter.getString(component_name)) != null ? text : "");
		try {
			String style = ini.getString(iniPath, component_name+"_fontStyle", true);
			b.setFont(new Font(ini.getString(iniPath, component_name+"_fontName", true), style.equals("PLAIN") ? Font.PLAIN : style.equals("BOLD") ? Font.BOLD : style.equals("ITALIC") ? Font.ITALIC : style.equals("BOLD+ITALIC") ? Font.ITALIC+Font.BOLD : Font.PLAIN, (int) Maths.format(ini.getString(iniPath, component_name+"_fontSize", true).replace("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height))));
		} catch(Exception e) { b.setFont(default_font); }
		try {
			b.setFontColorF(new Color(Integer.parseInt(ini.getString(iniPath, component_name+"_fontColor", true))));
		} catch(Exception e) { b.setFontColorF(default_fontColorF); }
		try {
			if(Boolean.parseBoolean(ini.getString(iniPath, component_name+"_border"))) b.setBorderWH((int) Maths.format(ini.getString(iniPath, component_name+"_borderW").replaceAll("%default%", ""+b.getBorderW()).replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width)), (int) Maths.format(ini.getString(iniPath, component_name+"_borderH").replaceAll("%default%", ""+b.getBorderH()).replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height)));
			else b.setBorderWH(0, 0);
		} catch(Exception e) {b.setBorderWH(default_borderW, default_borderH); }
		Dimension bMinSize = null;
		Dimension bMaxSize = null;
		Dimension bPreferredSize = null;
		String s = null;
		if (Boolean.parseBoolean((s = ini.getString(iniPath, component_name+"_img", true)) != null ? s : "false")) {
			b.setBackgroundToImage(new Image[] {ImageLoader.getImage(ini.getString(iniPath, component_name)), ImageLoader.getImage(ini.getString(iniPath, component_name+"F")), ImageLoader.getImage(ini.getString(iniPath, component_name+"P"))});
			bMinSize = new Dimension((int) Maths.format(ini.getString(iniPath, component_name+"_minW").replaceAll("%resolution%", ""+ImageLoader.toBufferedImage(b.getBackgroundImage()[0]).getWidth())), (int) Maths.format(ini.getString(iniPath, component_name+"_minH").replaceAll("%resolution%", ""+ImageLoader.toBufferedImage(b.getBackgroundImage()[0]).getHeight())));
			bMaxSize = new Dimension((int) Maths.format(ini.getString(iniPath, component_name+"_maxW").replaceAll("%resolution%", ""+ImageLoader.toBufferedImage(b.getBackgroundImage()[0]).getWidth())), (int) Maths.format(ini.getString(iniPath, component_name+"_maxH").replaceAll("%resolution%", ""+ImageLoader.toBufferedImage(b.getBackgroundImage()[0]).getHeight())));
		} else {
			try {
				// TODO: Make this line working --> rewrite rendering
				b.setBackground(new Color(Integer.parseInt(ini.getString(iniPath, component_name+"_backgroundColor", true))));
			} catch(Exception e) {}
			bMinSize = new Dimension((int) Maths.format(ini.getString(iniPath, component_name+"_minW").replaceAll("%resolution%", ""+(((int)b.getFont().getStringBounds(b.getLabel(), ((Graphics2D) b.getGraphics()).getFontRenderContext()).getWidth())/2+minTextMarginW))), (int) Maths.format(ini.getString(iniPath, component_name+"_minH").replaceAll("%resolution%", ""+((int)((b.getFont().getSize())/2+(1.0/4)*b.getFont().getSize()))+minTextMarginH)));
			bMaxSize = new Dimension((int) Maths.format(ini.getString(iniPath, component_name+"_maxW").replaceAll("INFINITE", ""+Integer.MAX_VALUE)), (int)Maths.format(ini.getString(iniPath, component_name+"_maxH").replaceAll("INFINITE", ""+Integer.MAX_VALUE)));
		}
		b.setMinimumSize(bMinSize);
		b.setMaximumSize(bMaxSize);
		bPreferredSize = new Dimension((int) Maths.format(ini.getString(iniPath, component_name+"_preferredW").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().width)), (int) Maths.format(ini.getString(iniPath, component_name+"_preferredH").replaceAll("%screensize%", ""+Toolkit.getDefaultToolkit().getScreenSize().height)));
		bPreferredSize = bPreferredSize.getWidth() > bMinSize.getWidth() && bPreferredSize.getHeight() > bMinSize.getHeight() ? (bPreferredSize.getWidth() < bMaxSize.getWidth() && bPreferredSize.getHeight() < bMaxSize.getHeight() ? bPreferredSize : bMaxSize) : bMinSize;
		b.setPreferredSize(bPreferredSize);
		if (Boolean.parseBoolean(ini.getString(iniPath, component_name+"_sameRatio"))) b.setBorderWH(b.getBorderH(), b.getBorderH());
		b.setBounds(0, 0, b.getPreferredSize().width, b.getPreferredSize().height);	// You will have to set the location at the point you need it
		b.setResizeable(Boolean.parseBoolean(ini.getString(iniPath, component_name+"_resizeable")));
		return b;
	}
	
	// Setters and Adders
	public void setButtonState(int buttonState) {
		this.buttonState = buttonState;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}

	public void setForegroundReleaseColor(Color fReleaseC) {
		this.fReleasedC = fReleaseC;
	}
	
	public void setBackgroundToImage(Image[] img) {
		backgroundImage = img;
	}

	public void setfReleasedC(Color fReleasedC) {
		this.fReleasedC = fReleasedC;
	}

	public void setfFocussedC(Color fFocussedC) {
		this.fFocussedC = fFocussedC;
	}

	public void setfPressedC(Color fPressedC) {
		this.fPressedC = fPressedC;
	}

	public void setbReleasedC(Color bReleasedC) {
		this.bReleasedC = bReleasedC;
	}

	public void setbFocussedC(Color bFocussedC) {
		this.bFocussedC = bFocussedC;
	}

	public void setbPressedC(Color bPressedC) {
		this.bPressedC = bPressedC;
	}

	public void setFontColorF(Color fontColorF) {
		this.fontColor = fontColorF;
	}

	public void setBorderWH(int borderW, int borderH) {
		this.borderW = borderW < minimumBorderW ? minimumBorderW : borderW;
		this.borderH = borderH < minimumBorderH ? minimumBorderH : borderH;
	}
	
	public void setResizeable(boolean b) {
		resize = b;
	}
	
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	
	// Getters
	public String getLabel() {
		return label;
	}
	
	public Color getfReleasedC() {
		return fReleasedC;
	}

	public Color getfFocussedC() {
		return fFocussedC;
	}
	
	public Color getfPressedC() {
		return fPressedC;
	}
	
	public Color getbReleasedC() {
		return bReleasedC;
	}

	public Color getbFocussedC() {
		return bFocussedC;
	}

	public Color getbPressedC() {
		return bPressedC;
	}

	public Color getFontColorF() {
		return fontColor;
	}

	public int getBorderW() {
		return borderW;
	}

	public int getBorderH() {
		return borderH;
	}

	public Image[] getBackgroundImage() {
		return backgroundImage;
	}

	public Font getFont() {
		return font;
	}

	public int getButtonState() {
		return buttonState;
	}
	
	public int getID() {
		return id;
	}

	public int getLayerIndex() {
		return layerIndex;
	}
	
	public boolean resizeable() {
		return resize;
	}
	
	// Fire Events
	public void fireActionEvent() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i+=2) {
			if (listeners[i] == ActionListener.class)
				((ActionListener) listeners[i+1]).actionPerformed(new ActionEvent(this, id, label));
		}
	}
	
	
	@Override
	public void paintComponent(Graphics go) {

		super.paintComponent(go);
		
		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Insets insets = getInsets();
		int x = insets.left;
		int y = insets.top;
		
		// Easy use of colors
		Color fToUse, bToUse;
		if (buttonState == buttonStates.FOCUSSED.id) {
			fToUse = fFocussedC;
			bToUse = bFocussedC;
		} else if (buttonState == buttonStates.PRESSED.id) {
			fToUse = fPressedC;
			bToUse = bPressedC;
		} else {
			fToUse = fReleasedC;
			bToUse = bReleasedC;
		}
		
		// Draw button-border
		g.setColor(bToUse);
		g.fillRect(x, y, (int)getSize().getWidth(), (int)getSize().getHeight());
		
		// Draw button
		if(backgroundImage != null) {
			Image imgToUse = backgroundImage[buttonState];
			BufferedImage img;
			if (imgToUse.equals(lastImage)) img = lastImageBuffered;
			else {
				lastImage = imgToUse;
				img = ImageLoader.toBufferedImage(ImageLoader.scale(ImageLoader.toBufferedImage(imgToUse), getSize().width-2*borderW, getSize().height-2*borderH));
				lastImageBuffered = img;
			}
			g.drawImage(img, x+borderW, y+borderH, img.getWidth(), img.getHeight(), null);
		} else {
			g.setColor(fToUse);
			g.fillRect(x+borderW, y+borderH, getSize().width-2*borderW, getSize().height-2*borderH);
		}
		
		g.setFont(font);
		g.setColor(fontColor);
		g.drawString(label, x+(getWidth()-((int)font.getStringBounds(label, g.getFontRenderContext()).getWidth()))/2, (int)(y+(getHeight()-((getHeight()-font.getSize())/2+(1.0/4)*font.getSize()))));
	}
	
}
