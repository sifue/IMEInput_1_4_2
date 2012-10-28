import java.awt.event.KeyEvent;


import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class ary extends asd
{
  private final asb a;
  private final int b;
  private final int c;
  private final int d;
  private final int e;
  private String f = "";
  private int g = 32;
  private int h;
  private boolean i = true;

  private boolean k = true;

  private boolean l = false;

  private boolean _m = true;
  private int n = 0;
  private int o = 0;

  private int p = 0;
  private int q = 14737632;
  private int r = 7368816;

  private boolean s = true;

  private static ary currentInstance = null;
	  
  public ary(asb par1FontRenderer, int par2, int par3, int par4, int par5)
  {
    this.a = par1FontRenderer;
    this.b = par2;
    this.c = par3;
    this.d = par4;
    this.e = par5;
    currentInstance = this;
    IMEInput.getInstance().put("chat", new IMEKeyTypedPreFilter() {
		@Override
		public void keyTyped(KeyEvent e) {
			  int charInt = (int)e.getKeyChar();
			  if((charInt == 13 || charInt == 10) // CR or LF
					 && currentInstance != null){
				currentInstance.b(IMEInput.getInstance().getText());
				IMEInput.getInstance().setText("");
				IMEInput.getInstance().update();
				IMEInput.getInstance().detach();
			  } else if(charInt == 8) { // BS
				  currentInstance.b(-1);
			  } else if(charInt == 127) { // DEL
				  currentInstance.b(-1);
			  } else if(!Character.isLetter(Character.forDigit(charInt, 10))) {
				  currentInstance.b(charInt);
			  }
		}
	});
    IMEInput.getInstance().attach();
    IMEInput.getInstance().field.requestFocus();	
  }

  public void a()
  {
    this.h += 1;
  }

  public void a(String par1Str)
  {
    if (par1Str.length() > this.g)
    {
      this.f = par1Str.substring(0, this.g);
    }
    else
    {
      this.f = par1Str;
    }

    e();
  }

  public String b()
  {
	
    return this.f;
  }

  public String c()
  {
    int var1 = this.o < this.p ? this.o : this.p;
    int var2 = this.o < this.p ? this.p : this.o;
    return this.f.substring(var1, var2);
  }

  public void b(String par1Str)
  {
    String var2 = "";
    String var3 = m.a(par1Str);
    int var4 = this.o < this.p ? this.o : this.p;
    int var5 = this.o < this.p ? this.p : this.o;
    int var6 = this.g - this.f.length() - (var4 - this.p);

    if (this.f.length() > 0)
    {
      var2 = var2 + this.f.substring(0, var4);
    }
    int var8;
    if (var6 < var3.length())
    {
      var2 = var2 + var3.substring(0, var6);
      var8 = var6;
    }
    else
    {
      var2 = var2 + var3;
      var8 = var3.length();
    }

    if ((this.f.length() > 0) && (var5 < this.f.length()))
    {
      var2 = var2 + this.f.substring(var5);
    }

    this.f = var2;
    d(var4 - this.p + var8);
  }

  public void a(int par1)
  {
    if (this.f.length() != 0)
    {
      if (this.p != this.o)
      {
        b("");
      }
      else
      {
        b(c(par1) - this.o);
      }
    }
  }

  public void b(int par1)
  {
    if (this.f.length() != 0)
    {
      if (this.p != this.o)
      {
        b("");
      }
      else
      {
        boolean var2 = par1 < 0;
        int var3 = var2 ? this.o + par1 : this.o;
        int var4 = var2 ? this.o : this.o + par1;
        String var5 = "";

        if (var3 >= 0)
        {
          var5 = this.f.substring(0, var3);
        }

        if (var4 < this.f.length())
        {
          var5 = var5 + this.f.substring(var4);
        }

        this.f = var5;

        if (var2)
        {
          d(par1);
        }
      }
    }
  }

  public int c(int par1)
  {
    return a(par1, h());
  }

  public int a(int par1, int par2)
  {
    return a(par1, h(), true);
  }

  public int a(int par1, int par2, boolean par3)
  {
    int var4 = par2;
    boolean var5 = par1 < 0;
    int var6 = Math.abs(par1);

    for (int var7 = 0; var7 < var6; var7++)
    {
      if (var5)
      {
        while ((par3) && (var4 > 0) && (this.f.charAt(var4 - 1) == ' '))
        {
          var4--;
        }

        while ((var4 > 0) && (this.f.charAt(var4 - 1) != ' '))
        {
          var4--;
        }

      }

      int var8 = this.f.length();
      var4 = this.f.indexOf(' ', var4);

      if (var4 == -1)
      {
        var4 = var8;
      }
      else
      {
        while ((par3) && (var4 < var8) && (this.f.charAt(var4) == ' '))
        {
          var4++;
        }
      }

    }

    return var4;
  }

  public void d(int par1)
  {
    e(this.p + par1);
  }

  public void e(int par1)
  {
    this.o = par1;
    int var2 = this.f.length();

    if (this.o < 0)
    {
      this.o = 0;
    }

    if (this.o > var2)
    {
      this.o = var2;
    }

    i(this.o);
  }

  public void d()
  {
    e(0);
  }

  public void e()
  {
    e(this.f.length());
  }
  

  public boolean a(char par1, int par2)
  {
	if(!((int)par1 == 0 && (par2 == 203 || par2 == 205))){
	 IMEInput.getInstance().attach();
	 IMEInput.getInstance().field.requestFocus();	
	}
	
    if ((this._m) && (this.l))
    {
      switch (par1)
      {
      case '\001':
        e();
        i(0);
        return true;
      case '\003':
        asw.d(c());
        return true;
      case '\026':
        b(asw.l());
        return true;
      case '\030':
        asw.d(c());
        b("");
        return true;
      }

      switch (par2)
      {
      case 14:
        if (asw.o())
        {
          a(-1);
        }
        else
        {
          b(-1);
        }
        
        return true;
      case 199:
        if (asw.p())
        {
          i(0);
        }
        else
        {
          d();
        }

        return true;
      case 203:
        if (asw.p())
        {
          if (asw.o())
          {
            i(a(-1, n()));
          }
          else
          {
            i(n() - 1);
          }
        }
        else if (asw.o())
        {
          e(c(-1));
        }
        else
        {
          d(-1);
        }

        return true;
      case 205:
        if (asw.p())
        {
          if (asw.o())
          {
            i(a(1, n()));
          }
          else
          {
            i(n() + 1);
          }
        }
        else if (asw.o())
        {
          e(c(1));
        }
        else
        {
          d(1);
        }

        return true;
      case 207:
        if (asw.p())
        {
          i(this.f.length());
        }
        else
        {
          e();
        }

        return true;
      case 211:
        if (asw.o())
        {
          a(1);
        }
        else
        {
          b(1);
        }

        return true;
      }

      
      if (m.a(par1))
      {
         b(Character.toString(par1));
        return true;
      }

      return false;
    }

    return false;
  }

  public void a(int par1, int par2, int par3)
  {
    boolean var4 = (par1 >= this.b) && (par1 < this.b + this.d) && (par2 >= this.c) && (par2 < this.c + this.e);

    if (this.k)
    {
      b((this._m) && (var4));
    }

    if ((this.l) && (par3 == 0))
    {
      int var5 = par1 - this.b;

      if (this.i)
      {
        var5 -= 4;
      }

      String var6 = this.a.a(this.f.substring(this.n), o());
      e(this.a.a(var6, var5).length() + this.n);
    }
  }

  public void f()
  {
    if (q())
    {
      if (i())
      {
        a(this.b - 1, this.c - 1, this.b + this.d + 1, this.c + this.e + 1, -6250336);
        a(this.b, this.c, this.b + this.d, this.c + this.e, -16777216);
      }

      int var1 = this._m ? this.q : this.r;
      int var2 = this.o - this.n;
      int var3 = this.p - this.n;
      String var4 = this.a.a(this.f.substring(this.n), o());
      boolean var5 = (var2 >= 0) && (var2 <= var4.length());
      boolean var6 = (this.l) && (this.h / 6 % 2 == 0) && (var5);
      int var7 = this.i ? this.b + 4 : this.b;
      int var8 = this.i ? this.c + (this.e - 8) / 2 : this.c;
      int var9 = var7;

      if (var3 > var4.length())
      {
        var3 = var4.length();
      }

      if (var4.length() > 0)
      {
        String var10 = var5 ? var4.substring(0, var2) : var4;
        var9 = this.a.a(var10, var7, var8, var1);
      }

      boolean var13 = (this.o < this.f.length()) || (this.f.length() >= g());
      int var11 = var9;

      if (!var5)
      {
        var11 = var2 > 0 ? var7 + this.d : var7;
      }
      else if (var13)
      {
        var11 = var9 - 1;
        var9--;
      }

      if ((var4.length() > 0) && (var5) && (var2 < var4.length()))
      {
        this.a.a(var4.substring(var2), var9, var8, var1);
      }

      if (var6)
      {
        if (var13)
        {
          asd.a(var11, var8 - 1, var11 + 1, var8 + 1 + this.a.b, -3092272);
        }
        else
        {
          this.a.a("_", var11, var8, var1);
        }
      }

      if (var3 != var2)
      {
        int var12 = var7 + this.a.a(var4.substring(0, var3));
        c(var11, var8 - 1, var12 - 1, var8 + 1 + this.a.b);
      }
    }
  }

  private void c(int par1, int par2, int par3, int par4)
  {
    if (par1 < par3)
    {
      int var5 = par1;
      par1 = par3;
      par3 = var5;
    }

    if (par2 < par4)
    {
      int var5 = par2;
      par2 = par4;
      par4 = var5;
    }

    azb var6 = azb.a;
    GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
    GL11.glDisable(3553);
    GL11.glEnable(3058);
    GL11.glLogicOp(5387);
    var6.b();
    var6.a(par1, par4, 0.0D);
    var6.a(par3, par4, 0.0D);
    var6.a(par3, par2, 0.0D);
    var6.a(par1, par2, 0.0D);
    var6.a();
    GL11.glDisable(3058);
    GL11.glEnable(3553);
  }

  public void f(int par1)
  {
    this.g = par1;

    if (this.f.length() > par1)
    {
      this.f = this.f.substring(0, par1);
    }
  }

  public int g()
  {
    return this.g;
  }

  public int h()
  {
    return this.o;
  }

  public boolean i()
  {
    return this.i;
  }

  public void a(boolean par1)
  {
    this.i = par1;
  }

  public void g(int par1)
  {
    this.q = par1;
  }

  public void h(int par1)
  {
    this.r = par1;
  }

  public void b(boolean par1)
  {
    if ((par1) && (!this.l))
    {
      this.h = 0;
    }

    this.l = par1;
  }

  public boolean l()
  {
    return this.l;
  }

  public void c(boolean par1)
  {
    this._m = par1;
  }

  public int n()
  {
    return this.p;
  }

  public int o()
  {
    return i() ? this.d - 8 : this.d;
  }

  public void i(int par1)
  {
    int var2 = this.f.length();

    if (par1 > var2)
    {
      par1 = var2;
    }

    if (par1 < 0)
    {
      par1 = 0;
    }

    this.p = par1;

    if (this.a != null)
    {
      if (this.n > var2)
      {
        this.n = var2;
      }

      int var3 = o();
      String var4 = this.a.a(this.f.substring(this.n), var3);
      int var5 = var4.length() + this.n;

      if (par1 == this.n)
      {
        this.n -= this.a.a(this.f, var3, true).length();
      }

      if (par1 > var5)
      {
        this.n += par1 - var5;
      }
      else if (par1 <= this.n)
      {
        this.n -= this.n - par1;
      }

      if (this.n < 0)
      {
        this.n = 0;
      }

      if (this.n > var2)
      {
        this.n = var2;
      }
    }
  }

  public void d(boolean par1)
  {
    this.k = par1;
  }

  public boolean q()
  {
    return this.s;
  }

  public void e(boolean par1)
  {
    this.s = par1;
  }
}