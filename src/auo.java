import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class auo extends asw
{
  private static final String b = m.a;

  protected String a = "Edit sign message:";
  private amj c;
  private int d;
  private int _m = 0;

  private static auo currentInstance = null;
  public auo(amj paramamj) {
    this.c = paramamj;
    currentInstance = this;
    IMEInput imeInput = IMEInput.getInstance();
    imeInput.put("sign", new IMEKeyTypedPreFilter() {
		@Override
		public void keyTyped(KeyEvent e) {
			  int charInt = (int)e.getKeyChar();
			  if((charInt == 13 || charInt == 10) // CR or LF
					 && currentInstance != null){
				IMEInput currentInput = IMEInput.getInstance();
				String text = currentInput.getText();
				if(text != null && !text.isEmpty()){
				  auo.this.c.a[auo.this._m] = auo.this.c.a[auo.this._m] + text;
				  currentInput.setText("");
				  currentInput.update();
				}else{
					currentInstance.a((char)10 , 28);
				}
			  } else if(charInt == 8) { // BS
				  currentInstance.a((char)0 , 14);
			  } else if(charInt == 127) { // DEL
				  currentInstance.a((char)0 , 14);
			  }
		}
	});
    imeInput.attach();
    imeInput.field.requestFocus();	
  }

  public void A_()
  {
    this.h.clear();
    Keyboard.enableRepeatEvents(true);
    this.h.add(new arl(0, this.f / 2 - 100, this.g / 4 + 120, "Done"));
    this.c.a(false);
  }

  public void b()
  {
    Keyboard.enableRepeatEvents(false);
    this.e.r().c(new eu(this.c.l, this.c.m, this.c.n, this.c.a));
    this.c.a(true);
  }

  public void c()
  {
    this.d += 1;
  }

  protected void a(arl paramarl)
  {
    if (!paramarl.g) return;

    if (paramarl.f == 0) {
      this.c.d();
      this.e.a((asw)null);
    }
  }

  protected void a(char paramChar, int paramInt)
  {
	IMEInput imeInput = IMEInput.getInstance();
	imeInput.setPreFilterMode("sign");
	if(paramChar == (char)10 && (paramInt == 28 || paramInt == 208)){
	  imeInput.setText("");
	  imeInput.update();
	  imeInput.field.requestFocus();
	}
    if (paramInt == 200) this._m = (this._m - 1 & 0x3);
    if ((paramInt == 208) || (paramInt == 28)) this._m = (this._m + 1 & 0x3);
    if ((paramInt == 14) && (this.c.a[this._m].length() > 0)) {
      this.c.a[this._m] = this.c.a[this._m].substring(0, this.c.a[this._m].length() - 1);
    }
    if ((b.indexOf(paramChar) >= 0) && (this.c.a[this._m].length() < 15))
    {
      int tmp161_158 = this._m;
      String[] tmp161_154 = this.c.a; tmp161_154[tmp161_158] = (tmp161_154[tmp161_158] + paramChar);
    }
  }

  public void a(int paramInt1, int paramInt2, float paramFloat)
  {
    z_();

    a(this.k, this.a, this.f / 2, 40, 16777215);

    GL11.glPushMatrix();
    GL11.glTranslatef(this.f / 2, 0.0F, 50.0F);
    float f1 = 93.75F;
    GL11.glScalef(-f1, -f1, -f1);
    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

    alf localalf = this.c.q();

    if (localalf == alf.aG) {
      float f2 = this.c.p() * 360 / 16.0F;
      GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);

      GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
    } else {
      int i = this.c.p();
      float f3 = 0.0F;

      if (i == 2) f3 = 180.0F;
      if (i == 4) f3 = 90.0F;
      if (i == 5) f3 = -90.0F;
      GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
    }

    if (this.d / 6 % 2 == 0) this.c.b = this._m;

    bby.a.a(this.c, -0.5D, -0.75D, -0.5D, 0.0F);
    this.c.b = -1;

    GL11.glPopMatrix();

    super.a(paramInt1, paramInt2, paramFloat);
  }
}