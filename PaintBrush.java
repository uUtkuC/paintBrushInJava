import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;



public class PaintBrush extends JFrame implements MouseInputListener {

    // so that I can redraw everytime
    private class cizimObjeleri{
        String type; // dikdortgen, oval, kalem
        int x_min, x_max, y_min,y_max;
        Color cl;
        public cizimObjeleri(int x_min,int x_max, int y_min, int y_max, String s, Color cl){
            this.x_max = x_max;
            this.x_min = x_min;
            this.y_max = y_max;
            this.y_min = y_min;
            type = s;
            this.cl = cl;
        }
    }
    // arraylist to store obejcts.
    ArrayList<cizimObjeleri> cizilenlerListem;

    cizimObjeleri sonTasinan;

    int x = 0;
    int y = 0; // first coordinates of the mouse
    int x2=0;   // after dragging current coordinates
    int y2=0;
    JButton lastPressed = null; // set the last pressed to this one.
    boolean keepDrawing = false;

    Color currentColor = Color.black;
    JPanel UstPanel = new JPanel();

    // following are buttons to draw
    JPanel UstPanelinAltCubugu = new JPanel();
    JPanel UstPanelinUstCubugu = new JPanel();
    JPanel AltPanel = new JPanel();
    JButton dikdortgen = new JButton("Draw Rectangle");
    JButton oval = new JButton("Draw Oval");
    JButton kalemle = new JButton("Pen");
    JButton Tasi = new JButton("Move"); // onlu for oval and dikdortgen

    //panel yap renklendir ekle. following are not buttons but panels. Purpose is to give color
     JPanel[] panellerArrayi = new JPanel[7];
    JPanel mavi = new JPanel();
     JPanel kirmizi = new JPanel();
     JPanel yesil = new JPanel();
     JPanel sari = new JPanel();
     JPanel turuncu = new JPanel();
     JPanel mor = new JPanel();
     JPanel siyah = new JPanel();
     JPanel soLBosluk = new JPanel();
     JPanel sagBOsluk = new JPanel();
    Integer yYiGecMezDeger = 0;
    Integer tasirkenCikarilacakDeger = 0;
    public boolean OvalIcindeMi(cizimObjeleri curr,int x, int y){ // problemli
        boolean condition = false;

        /*if(Math.abs((Math.pow(Math.abs(x-Math.abs(curr.x_max-curr.x_min)/2),2))/Math.pow(((double)(Math.abs(curr.x_max-curr.x_min))/2),2)+
                Math.pow(Math.abs(y-Math.abs(curr.y_max-curr.y_min)/2),2)/Math.pow(((double)(Math.abs(curr.y_max-curr.y_min))/2),2) -1)<=0.5 )*/
       // condition=true;
        //double cikarX = (double) Math.abs((double) (curr.x_max-curr.x_min))/2;
       // double cikarY = (double) Math.abs((double) (curr.y_max-curr.y_min))/2;
        //condition = ((x-cikarX)*(x-cikarX)/Math.pow(cikarX,2)+ (y-cikarY)*(y-cikarY)/Math.pow((cikarY),2) <= Math.pow(cikarX*cikarY,2));

        double xUzunlugu = Math.pow(Math.abs(Math.abs(((double)curr.x_min+curr.x_max)/2)-this.x),2) / Math.pow((double) Math.abs(curr.x_min-curr.x_max)/2,2);
        double yUzunlugu = Math.pow(Math.abs(Math.abs(((double)curr.y_min+curr.y_max)/2)-this.y),2) / Math.pow((double) Math.abs(curr.y_min-curr.y_max)/2,2);
      //  System.out.println(curr.y_min+" ymax: "+ curr.y_max+" xmin:"+curr.x_min +" "+curr.x_max+" ovalin:"+x +" y"+ y +"  thisx:"+ this.x+" "+this.y);
       // System.out.println(xUzunlugu+ " y:"+ yUzunlugu);

        condition = xUzunlugu +yUzunlugu <=1;


        return condition;
    }
    int width, height;
    public PaintBrush(){
        // give size, change colour, addActionListener ,make visible of the following
        // adds color to panels
       addColorToPanels();
       cizilenlerListem = new ArrayList<cizimObjeleri>();

        panellerArrayi[0] = mavi;
        panellerArrayi[1] = kirmizi;
        panellerArrayi[2] = yesil;
        panellerArrayi[3] = sari;
        panellerArrayi[4] = turuncu;
        panellerArrayi[5] = mor;
        panellerArrayi[6] = siyah;
        UstPanelinUstCubugu.setLayout(new GridLayout());
        UstPanelinUstCubugu.add(soLBosluk);

        for(JPanel p : panellerArrayi){
            p.setSize(20,30);
            p.setVisible(true);
            UstPanelinUstCubugu.add(p);
        }
        UstPanelinUstCubugu.add(sagBOsluk);

        mavi.setBackground(Color.blue);
        kirmizi.setBackground(Color.red);
        yesil.setBackground(Color.green);
        sari.setBackground(Color.yellow);
        turuncu.setBackground(Color.orange);
        mor.setBackground(Color.magenta);
        siyah.setBackground(Color.black);

        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);

        setLayout(new BorderLayout());
        UstPanel.setLayout(new BorderLayout());
        UstPanelinAltCubugu.add(dikdortgen,BorderLayout.NORTH);
        UstPanelinAltCubugu.add(oval,BorderLayout.NORTH);
        UstPanelinAltCubugu.add(kalemle,BorderLayout.NORTH);
        UstPanelinAltCubugu.add(Tasi,BorderLayout.NORTH);

        UstPanel.setVisible(true);
        UstPanelinAltCubugu.setVisible(true);
        UstPanelinUstCubugu.setVisible(true);
        UstPanel.add(UstPanelinAltCubugu,BorderLayout.CENTER);
        UstPanel.add(UstPanelinUstCubugu,BorderLayout.NORTH);



        JPanel maviCizgi = new JPanel();
        maviCizgi.setSize(10,10);
        maviCizgi.setBackground(Color.blue);
        maviCizgi.setVisible(true);
        UstPanel.add(maviCizgi,BorderLayout.SOUTH);
        add(UstPanel,BorderLayout.NORTH);
        AltPanel.setVisible(true);
        add(AltPanel,BorderLayout.CENTER);
        // sets action lsiteners to dikdortgen oval kamleCiz and tasi
        setActionListenersTOBUttons();
        setVisible(true);
        yYiGecMezDeger = AltPanel.getY()+31;

        width = this.getWidth();
        height = this.getHeight();
        //  System.out.println(height +" "+ width);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                yonlendir();
            }
        });

    }
    public void yonlendir(){
        ArePaintThreadiAc(this);
    }
    public void ArePaintThreadiAc(JFrame jFrame) {
        Thread t = new Thread(){
            int width2 = (int)jFrame.getWidth();
            int height2 = (int)jFrame.getHeight();
            @Override
            public void run() {
                if(width2 != width || height2 != height){
                    for(int n=0;n<20 ;n++)
                    Arepaint();
                    if(width2!=width) width =width2;
                    if(height2!= height) height = height2;
              //      System.out.println(height2 +" "+ width2);
                }
            }
        };
        t.start();

    }


    @Override
    public void mouseClicked(MouseEvent e) {

        if(lastPressed == null){

        }else if(lastPressed == dikdortgen){

        }else if(lastPressed == oval){

        }else if(lastPressed == kalemle){

        }else if(lastPressed == Tasi){

        }
    }
    boolean Iclick = false;
    @Override
    public void mousePressed(MouseEvent e) {
        isItOnShape = false;
        if(lastPressed == null || e.getY()<= yYiGecMezDeger){

        }else {
            Iclick = true;
            keepDrawing = true;
            x = e.getX();
            y = e.getY();
        }
        if(lastPressed == Tasi){
            // if tasi, sekil icinde mi diye bakacagiz.
            for(cizimObjeleri curr : cizilenlerListem){
                if(curr.x_max>=x && curr.x_min<=x &&curr.y_max>=y && curr.y_min<=y ){ // since the one to the top is at the end of the list it makes sense to move from end to beggining.
                    if(curr.type.equals("dikdortgen")){ isItOnShape = true;}
                    else if(curr.type.equals("oval")){if(OvalIcindeMi(curr,x,y)==true) isItOnShape = true;}

                }else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max>=y && curr.y_min<=y ){
                    if(curr.type.equals("dikdortgen")){ isItOnShape = true;}
                    else if(curr.type.equals("oval")){if(OvalIcindeMi(curr,x,y)==true) isItOnShape = true;}

                } else if(curr.x_max>=x && curr.x_min<=x &&curr.y_max<=y && curr.y_min>=y){
                    if(curr.type.equals("dikdortgen")){ isItOnShape = true;}
                    else if(curr.type.equals("oval")){if(OvalIcindeMi(curr,x,y)==true) isItOnShape = true;}

                } else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max<=y && curr.y_min>=y ){
                    if(curr.type.equals("dikdortgen")){ isItOnShape = true;}
                    else if(curr.type.equals("oval")){if(OvalIcindeMi(curr,x,y)==true) isItOnShape = true;}

                }
            }
            x2 = x;
            y2 = y;
        }
    }
    cizimObjeleri buffer;
    @Override
    public void mouseReleased(MouseEvent e) {  int width2 = (int)this.getWidth();
        isItOnShape = false;
        keepDrawing = false;
        if(Iclick == true){
        if(lastPressed == null){
        }else if (e.getY()<= yYiGecMezDeger){
            if(buffer!=null)
            cizilenlerListem.add(buffer);
        } else if(lastPressed == dikdortgen){
            x2 = e.getX();
            y2 = e.getY();
            dikdorgenCiz();
            cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"dikdortgen",currentColor));

        }else if(lastPressed == oval){
            x2 = e.getX();
            y2 = e.getY();
            ovalCiz();
            cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"oval",currentColor));

        }else if(lastPressed == kalemle){
            x = e.getX();
            y = e.getY();
            KalemleCiz();
            cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"kalem",currentColor)); // use x and y

        }else if(lastPressed == Tasi){ // draw the latest shape
            Tasi();
        }
        Iclick = false;
    }
    }
    boolean isItOnShape = false;
    @Override
    public void mouseDragged(MouseEvent e) { // if tasi is true then we have a known shape. fixed size.
        if(e.getY()>yYiGecMezDeger)
            Arepaint();
        if(((((lastPressed == null || keepDrawing == false) && lastPressed != Tasi) || e.getY()< yYiGecMezDeger)) &&Iclick == true){ // if false or null do nothing
            if(((lastPressed == null || keepDrawing == false) && lastPressed != Tasi) == false && e.getY()==yYiGecMezDeger-1){

                if(lastPressed == null){

                }else if(lastPressed == dikdortgen){
                    x2 = e.getX();
                    y2 = e.getY();
                    dikdorgenCiz();
                  //  cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"dikdortgen",currentColor));
                    buffer =new cizimObjeleri(x,x2,y,y2,"dikdortgen",currentColor);
                }else if(lastPressed == oval){
                    x2 = e.getX();
                    y2 = e.getY();
                    ovalCiz();
                  //  cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"oval",currentColor));
                    buffer =new cizimObjeleri(x,x2,y,y2,"oval",currentColor);
                }else if(lastPressed == kalemle){
                    x = e.getX();
                    y = e.getY();
                    KalemleCiz();
                   // cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"kalem",currentColor)); // use x and y
                    buffer = new cizimObjeleri(x,x2,y,y2,"kalem",currentColor);
                }
            }
        }else if(lastPressed == dikdortgen&&Iclick == true){
            x2 = e.getX();
            y2 = e.getY();
            dikdorgenCiz();

        }else if(lastPressed == oval&&Iclick == true){
            x2 = e.getX();
            y2 = e.getY();
            ovalCiz();

        }else if(lastPressed == kalemle&&Iclick == true){
            x = e.getX();
            y = e.getY();
            Graphics g = getGraphics();
            g.setColor(currentColor);
            g.fillOval(x,y,4,4);
            cizilenlerListem.add( new cizimObjeleri(x,x2,y,y2,"kalem",currentColor)); // use x and y

        }else if(lastPressed == Tasi&&Iclick == true&& isItOnShape == true){
            x = e.getX();
            y = e.getY();
            Tasi();
        }
    }
    // move enter exit e gerek yok
    public void mouseEntered(MouseEvent e) {
    }public void mouseExited(MouseEvent e) {
    }public void mouseMoved(MouseEvent e) {
    }

    public static void main(String[] args) {
        PaintBrush pb = new PaintBrush();

    }

    public void Tasi(){
      //  System.out.println("Girdi");
        for(int n= cizilenlerListem.size()-1;n>=0 ;n--){ // ilk yer x2 y2
            cizimObjeleri curr = cizilenlerListem.get(n);
            if(curr!= null && curr.type.equals("kalem") == false )
               if(curr.type.equals("dikdortgen")){

                if(curr.x_max>=x && curr.x_min<=x &&curr.y_max>=y && curr.y_min<=y ){ // since the one to the top is at the end of the list it makes sense to move from end to beggining.
                 //   System.out.println("Gördüm");
                    int xTasinanin = curr.x_min;
                    int yTasinanin = curr.y_min;
                    int soldanSagaFark = x2-x;
                    int asagidanYukariFark = y2-y;
                    if( (curr.y_min<=yYiGecMezDeger && y<y2)==false)
                    curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                    x2 = x;
                    y2= y;
                    cizilenlerListem.remove(n);
                    cizilenlerListem.add(curr);

                    break;
                }else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max>=y && curr.y_min<=y ){
                   // System.out.println("Gördüm");
                    int xTasinanin = curr.x_min;
                    int yTasinanin = curr.y_min;
                    int soldanSagaFark = x2-x;
                    int asagidanYukariFark = y2-y;
                    if( (curr.y_min<=yYiGecMezDeger && y<y2)==false)
                    curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                    x2 = x;
                    y2= y;
                    cizilenlerListem.remove(n);
                    cizilenlerListem.add(curr);

                    break;
                } else if(curr.x_max>=x && curr.x_min<=x &&curr.y_max<=y && curr.y_min>=y){
                   // System.out.println("Gördüm");
                    int xTasinanin = curr.x_min;
                    int yTasinanin = curr.y_min;
                    int soldanSagaFark = x2-x;
                    int asagidanYukariFark = y2-y;
                    if((curr.y_max<= yYiGecMezDeger && y<y2) == false)
                    curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                    x2 = x;
                    y2= y;

                    cizilenlerListem.remove(n);
                    cizilenlerListem.add(curr);
                    break;
                } else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max<=y && curr.y_min>=y ){
                  //  System.out.println("Gördüm");
                    int xTasinanin = curr.x_min;
                    int yTasinanin = curr.y_min;
                    int soldanSagaFark = x2-x;
                    int asagidanYukariFark = y2-y;
                    if((curr.y_max<= yYiGecMezDeger && y<y2) == false)
                    curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                    x2 = x;
                    y2= y;
                    cizilenlerListem.remove(n);
                    cizilenlerListem.add(curr);
                    break;
                    }
                }
            else if(curr.type.equals("oval") && OvalIcindeMi(cizilenlerListem.get(n),curr.x_min,curr.y_min)) {

                   if(curr.x_max>=x && curr.x_min<=x &&curr.y_max>=y && curr.y_min<=y ){ // since the one to the top is at the end of the list it makes sense to move from end to beggining.
                       //   System.out.println("Gördüm");
                       int xTasinanin = curr.x_min;
                       int yTasinanin = curr.y_min;
                       int soldanSagaFark = x2-x;
                       int asagidanYukariFark = y2-y;
                       if( (curr.y_min<=yYiGecMezDeger && y<y2)==false)
                           curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                       x2 = x;
                       y2= y;
                       cizilenlerListem.remove(n);
                       cizilenlerListem.add(curr);

                       break;
                   }else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max>=y && curr.y_min<=y ){
                       // System.out.println("Gördüm");
                       int xTasinanin = curr.x_min;
                       int yTasinanin = curr.y_min;
                       int soldanSagaFark = x2-x;
                       int asagidanYukariFark = y2-y;
                       if( (curr.y_min<=yYiGecMezDeger && y<y2)==false)
                           curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                       x2 = x;
                       y2= y;
                       cizilenlerListem.remove(n);
                       cizilenlerListem.add(curr);

                       break;
                   } else if(curr.x_max>=x && curr.x_min<=x &&curr.y_max<=y && curr.y_min>=y){
                       // System.out.println("Gördüm");
                       int xTasinanin = curr.x_min;
                       int yTasinanin = curr.y_min;
                       int soldanSagaFark = x2-x;
                       int asagidanYukariFark = y2-y;
                       if((curr.y_max<= yYiGecMezDeger && y<y2) == false)
                           curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                       x2 = x;
                       y2= y;

                       cizilenlerListem.remove(n);
                       cizilenlerListem.add(curr);
                       break;
                   } else if(curr.x_max<=x && curr.x_min>=x &&curr.y_max<=y && curr.y_min>=y ){
                       //  System.out.println("Gördüm");
                       int xTasinanin = curr.x_min;
                       int yTasinanin = curr.y_min;
                       int soldanSagaFark = x2-x;
                       int asagidanYukariFark = y2-y;
                       if((curr.y_max<= yYiGecMezDeger && y<y2) == false)
                           curr = new cizimObjeleri(xTasinanin+(-x2+x),curr.x_max+(-x2+x),yTasinanin+(-y2+y),curr.y_max+(-y2+y),curr.type,curr.cl);
                       x2 = x;
                       y2= y;
                       cizilenlerListem.remove(n);
                       cizilenlerListem.add(curr);
                       break;
                   }
               }
        }
    }
    public void Arepaint(){ // repaints old screen
        Graphics g = getGraphics();
        super.paint(g);
        for(cizimObjeleri c : cizilenlerListem){ // draw all
            if(c.type.equals("dikdortgen")){
                g.setColor(c.cl);
                if(c.x_min<c.x_max && c.y_min<c.y_max)
                    g.fillRect(c.x_min,c.y_min,Math.abs(c.x_min-c.x_max),Math.abs(c.y_max-c.y_min));
                else if(c.x_min<c.x_max && c.y_min>c.y_max)
                    g.fillRect(c.x_min,c.y_max,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
                else if(c.x_min>c.x_max && c.y_min<c.y_max)
                    g.fillRect(c.x_max,c.y_min,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
                else
                    g.fillRect(c.x_max,c.y_max,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
            } else if( c.type.equals("oval")){
                g.setColor(c.cl);
                if(c.x_min<c.x_max && c.y_min<c.y_max)
                    g.fillOval(c.x_min,c.y_min,Math.abs(c.x_min-c.x_max),Math.abs(c.y_max-c.y_min));
                else if(c.x_min<c.x_max && c.y_min>c.y_max)
                    g.fillOval(c.x_min,c.y_max,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
                else if(c.x_min>c.x_max && c.y_min<c.y_max)
                    g.fillOval(c.x_max,c.y_min,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
                else
                    g.fillOval(c.x_max,c.y_max,Math.abs(c.x_max-c.x_min),Math.abs(c.y_max-c.y_min));
            } else{ // noktadır
                g.setColor(c.cl);
                g.fillOval(c.x_min,c.y_min,4,4);
            }
        }
    }


    // methods to draw
    public void dikdorgenCiz(){
        Graphics g = getGraphics();
        g.setColor(currentColor);
        if(x<x2 && y<y2){
            g.fillRect(x,y,Math.abs(x2-x),Math.abs(y2-y));
            tasirkenCikarilacakDeger = Math.abs(y2-y);
        }
        else if(x<x2 && y>y2){
            g.fillRect(x,y2,Math.abs(x2-x),Math.abs(y2-y));
            tasirkenCikarilacakDeger = Math.abs(y2-y);
        }
        else if(x>x2 && y<y2){
            g.fillRect(x2,y,Math.abs(x2-x),Math.abs(y2-y));
            tasirkenCikarilacakDeger = Math.abs(y2-y);
        }
        else{
            g.fillRect(x2,y2,Math.abs(x2-x),Math.abs(y2-y));
            tasirkenCikarilacakDeger = Math.abs(y2-y);
        }
    }
    public void ovalCiz(){
        Graphics g = getGraphics();
        g.setColor(currentColor);
        if(x<x2 && y<y2)
            g.fillOval(x,y,Math.abs(x2-x),Math.abs(y2-y));
        else if(x<x2 && y>y2)
            g.fillOval(x,y2,Math.abs(x2-x),Math.abs(y2-y));
        else if(x>x2 && y<y2)
            g.fillOval(x2,y,Math.abs(x2-x),Math.abs(y2-y));
        else
            g.fillOval(x2,y2,Math.abs(x2-x),Math.abs(y2-y));

    }
    public void KalemleCiz(){
        Graphics g = getGraphics();
        g.setColor(currentColor);
        g.fillOval(x,y,4,4);

    }

    public void setActionListenersTOBUttons(){
        dikdortgen.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
            lastPressed = dikdortgen;
                keepDrawing = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        oval.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                lastPressed = oval;
                keepDrawing = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        kalemle.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                lastPressed = kalemle;
                keepDrawing = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        Tasi.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                lastPressed = Tasi;
                keepDrawing = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    public void addColorToPanels(){
        mavi.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.blue;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        kirmizi.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.red;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        yesil.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.green;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        sari.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.yellow;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        turuncu.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.orange;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        mor.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.magenta;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        siyah.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentColor = Color.black;
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }


}
