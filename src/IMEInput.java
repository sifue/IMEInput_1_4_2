import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import net.minecraft.client.Minecraft;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class IMEInput
  implements KeyListener, InputMethodListener, CaretListener, MouseWheelListener
{
  private static IMEInput singleton = null;
  
  public static IMEInput getInstance(){
	  if(singleton == null){
		  singleton = new IMEInput(Minecraft.x());
	  }
	  return singleton;
  }
	
  protected final IMEKeyMap keyMap = new IMEKeyMap();
  protected Minecraft minecraft;
  protected Frame frame;
  protected JTextField field;
  protected JPanel panel;
  protected Container cont;
  protected Method keyTypedMethod;
  protected int limitLength = 20;
  protected volatile boolean isTakingScreenshot;
  protected volatile boolean isAttached;
  protected volatile boolean isFocusLost;
  protected volatile boolean isShiftKeyDown;
  private boolean isInputting = false;

  private String text = "";

  private int inputStartPos = 0;
  private AttributedCharacterIterator inputText;
  private String committedString = "";

  private String unCommittedString = "";
  private int inputTextCaret = 0;

  private int committedCharacterCount = 0;

  private int caretPos = 0;

  private boolean showField = true;

  private Runnable requestFieldFocus = new Runnable()
  {
    public void run() {
      IMEInput.this.field.requestFocusInWindow();
    }
  };

  public String getText()
  {
    synchronized (this)
    {
      String getText;
      if ((this.isInputting) && (!this.showField)) {
        StringBuilder sb = new StringBuilder(this.text);
        sb.insert(this.inputStartPos, this.unCommittedString);
        getText = sb.toString();
      } else {
        getText = this.field.getText();
      }
      return getText;
    }
  }

  public String getCommittedBeforeText()
  {
    synchronized (this) {
      return this.text;
    }
  }

  public void setText(String text)
  {
    synchronized (this) {
      this.field.setText(text);
    }
  }

  public void insert(String s, int pos)
  {
    synchronized (this) {
      StringBuilder sb = new StringBuilder(this.field.getText());
      sb.insert(pos, s);
      int caretPos = this.field.getCaretPosition() + s.length();
      this.field.setText(sb.toString());
      this.field.setCaretPosition(caretPos);
    }
  }

  public void setCaret(int pos)
  {
    synchronized (this) {
      this.field.setCaretPosition(pos);
    }
  }

  public void setSelect(int pos1, int pos2)
  {
    synchronized (this) {
      if (pos1 > pos2) {
        int tmp = pos1;
        pos1 = pos2;
        pos2 = tmp;
      }

      this.field.select(pos1, pos2);
    }
  }

  public void setCaretEnd()
  {
    synchronized (this) {
      if (this.field.getText().length() > 0)
        this.field.setCaretPosition(getText().length());
    }
  }

  public boolean isInputting()
  {
    synchronized (this) {
      return this.isInputting;
    }
  }

  public String getSelected()
  {
    synchronized (this) {
      return this.field.getSelectedText();
    }
  }

  public int getSelectedStart()
  {
    synchronized (this) {
      return this.field.getSelectionStart();
    }
  }

  public int getSelectedEnd()
  {
    synchronized (this) {
      return this.field.getSelectionEnd();
    }
  }

  public boolean isSelected()
  {
    synchronized (this) {
      try {
        return this.field.getSelectedText() != null;
      } catch (IllegalArgumentException e) {
        return false;
      }
    }
  }

  public AttributedCharacterIterator getInputText()
  {
    synchronized (this) {
      return this.inputText;
    }
  }

  public int getInputTextPos()
  {
    synchronized (this) {
      return this.inputStartPos;
    }
  }

  public int getCaretPos()
  {
    synchronized (this) {
      if (this.isInputting) {
        return this.inputStartPos + this.inputTextCaret;
      }
      return this.caretPos;
    }
  }

  public int getCommittedCharacterCount()
  {
    synchronized (this) {
      return this.committedCharacterCount;
    }
  }

  public void showField(boolean flag) {
    if (flag) {
      this.showField = true;
      this.field.setBackground(Color.WHITE);
      this.field.setPreferredSize(new Dimension(this.minecraft.c, 30));
    } else {
      this.showField = false;
      this.field.setBackground(Color.BLACK);
      this.field.setPreferredSize(new Dimension(this.minecraft.c, 1));
    }
  }

  public boolean isShowField() { return this.showField; }


  public int getLimitLength()
  {
    return this.limitLength;
  }

  public void setLimitLength(int limitLength) {
    this.limitLength = limitLength;
  }

  public IMEInput(final Minecraft minecraft)
  {
    this.minecraft = minecraft;

    Container c = null;
    for (c = minecraft.l.getParent(); (c != null) && (!(c instanceof Frame)); c = c.getParent());
    if (c == null) {
      throw new RuntimeException("ウィンドウの取得に失敗しました。");
    }
    this.frame = ((Frame)c);

    final IMEInput self = this;
    try {
		EventQueue.invokeAndWait(new Runnable()
		{
		  public void run() {
		    IMEInput.this.field = new JTextField();
		    IMEInput.this.field.addKeyListener(self);
		    IMEInput.this.field.setFont(new Font("SansSerif", 0, 22));
		    IMEInput.this.field.setBorder(BorderFactory.createEmptyBorder(0, 26, 0, 0));
		    IMEInput.this.field.setFocusTraversalKeysEnabled(false);

		    IMEInput.this.field.addCaretListener(self);
		    IMEInput.this.field.addInputMethodListener(self);

		    IMEInput.this.field.addFocusListener(new FocusListener()
		    {
		      public void focusLost(FocusEvent e) {
		        IMEInput.this.isFocusLost = true;
		      }

		      public void focusGained(FocusEvent e)
		      {
		        IMEInput.this.isFocusLost = false;
		      }
		    });
		    IMEInput.this.frame.addMouseWheelListener(self);

		    IMEInput.this.field.setVisible(false);

		    IMEInput.this.field.setPreferredSize(new Dimension(minecraft.c, 30));

		    IMEInput.this.frame.add(IMEInput.this.field, "South");
		  }
		});
	} catch (InterruptedException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	}
    this.keyTypedMethod = null;
    for (Method method : asw.class.getDeclaredMethods()) {
      Class[] params = method.getParameterTypes();
      if ((params.length == 2) && (params[0] == Character.TYPE) && (params[1] == Integer.TYPE)) {
        this.keyTypedMethod = method;
        this.keyTypedMethod.setAccessible(true);
        break;
      }
    }
    if (this.keyTypedMethod == null)
      throw new RuntimeException("NihongoMODの初期化に失敗しました。");
  }

  public void attach()
  {
    synchronized (this) {
      if (!this.isAttached) {
        this.isFocusLost = true;
        this.isAttached = true;

        switchField(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        this.frame.requestFocus();
      }
    }
  }

  public void detach()
  {
    if (this.isAttached) {
      this.isAttached = false;
      FocusEvent e = new FocusEvent(this.field, 1005);
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
      EventQueue.invokeLater(new Runnable()
      {
        public void run() {
          IMEInput.this.minecraft.l.requestFocusInWindow();
          IMEInput.this.minecraft.h();
        }
      });
    }
    try
    {
      Keyboard.destroy();
      Keyboard.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }

  }

  public void update()
  {
    if (this.isAttached) {
      if (Display.isActive()) {
        FocusEvent e = new FocusEvent(this.minecraft.l, 1005);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
      }

      if (this.isFocusLost) {
        EventQueue.invokeLater(this.requestFieldFocus);
      }
    }
    else if ((Display.isActive()) && 
      (this.field.isVisible())) {
      switchField(false);
      this.minecraft.h();
    }

    if (this.isTakingScreenshot) {
      this.isTakingScreenshot = false;
    }
  }

  protected void switchField(final boolean attach)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run() {
        if (attach)
        {
          IMEInput.this.field.setVisible(true);
        } else {
          IMEInput.this.field.setVisible(false);
          IMEInput.this.field.setText("");
        }

        if (IMEInput.this.getMaximizedMode() > 0) {
          IMEInput.this.frame.doLayout();
        } else {
          IMEInput.this.minecraft.l.setPreferredSize(IMEInput.this.minecraft.l.getSize());
          IMEInput.this.frame.pack();
        }
      }
    });
  }

  private int getMaximizedMode()
  {
    boolean isMaxmized = (this.frame.getExtendedState() & 0x6) == 6;
    if (isMaxmized) {
      return 6;
    }
    Point p = this.frame.getLocation();
    for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      for (GraphicsConfiguration gc : gd.getConfigurations()) {
        if (gc.getBounds().contains(p))
        {
          DisplayMode dm = gd.getDisplayMode();
          int dw = dm.getWidth();
          int dh = dm.getHeight();

          if (this.frame.getHeight() == dh) {
            return 4;
          }

          if (this.frame.getWidth() == dw) {
            return 2;
          }
        }
      }

    }

    return 0;
  }

  protected void sendTypedKey(asw gui, char c, int i)
  {
    try
    {
      this.keyTypedMethod.invoke(gui, new Object[] { Character.valueOf(c), Integer.valueOf(i) });
    }
    catch (Exception e)
    {
    }
  }
  
  private final Map<String, IMEKeyTypedPreFilter> keyTypedFilterMap = new ConcurrentHashMap<String, IMEKeyTypedPreFilter>();
  private final AtomicReference<String> filterMode = new AtomicReference<String>("");

  public IMEKeyTypedPreFilter put(String modeName, IMEKeyTypedPreFilter filter) {
	filterMode.set(modeName); //set filter mode 
	return keyTypedFilterMap.put(modeName, filter);
  }

  public void keyTyped(KeyEvent e)
  {
	keyTypedFilterMap.get(filterMode.get()).keyTyped(e);
	  
    if ((Character.isLetter(e.getKeyChar())) && 
      (this.field.getText().length() >= this.limitLength))
      e.consume();
  }

  public void keyPressed(KeyEvent e)
  {
     this.isShiftKeyDown = e.isShiftDown();
  }

  public void keyReleased(KeyEvent e)
  {
      this.isShiftKeyDown = e.isShiftDown();
  }

  public void caretPositionChanged(InputMethodEvent event)
  {
  }

  public void inputMethodTextChanged(InputMethodEvent event)
  {
    synchronized (this)
    {
      this.inputText = event.getText();
      this.inputTextCaret = (event.getCaret() == null ? 0 : event.getCaret().getCharIndex());

      if (event.getText() != null) {
        if (isSelected()) {
          String s1 = this.text.substring(0, getSelectedStart());
          String s2 = this.text.substring(getSelectedEnd());
          this.inputStartPos -= this.field.getSelectedText().length();
          this.text = (s1 + s2);
          this.field.setText(this.text);
        }
        this.committedCharacterCount = event.getCommittedCharacterCount();
        StringBuffer textBuffer = new StringBuffer();
        StringBuffer textBuffer2 = new StringBuffer();
        char c = this.inputText.first();
        int count = 0;
        while (this.committedCharacterCount > count) {
          textBuffer.append(c);
          c = this.inputText.next();
          count++;
        }
        while (c != 65535) {
          textBuffer2.append(c);
          c = this.inputText.next();
        }
        this.committedString = textBuffer.toString();
        this.unCommittedString = textBuffer2.toString();

        if ((this.committedString.length() != 0) && (!this.showField)) {
          StringBuilder sb = new StringBuilder(this.text);
          sb.insert(this.inputStartPos, this.committedString);
          this.text = sb.toString();
          this.inputStartPos += this.committedString.length();
          this.field.setText(this.text);
          this.field.setCaretPosition(this.inputStartPos);
        }

        this.isInputting = (this.unCommittedString.length() != 0);
      } else {
        this.committedString = "";
        this.unCommittedString = "";
        this.committedCharacterCount = 0;
        this.isInputting = false;
      }
      if (!this.showField)
        event.consume();
    }
  }

  public void caretUpdate(CaretEvent e)
  {
    synchronized (this) {
      if (!this.isInputting) {
        this.text = this.field.getText();
        this.inputStartPos = e.getDot();
      }
      this.caretPos = e.getDot();
    }
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
  }
}