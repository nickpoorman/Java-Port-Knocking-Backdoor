//	Project : Basic D3D Menu v1.2
//	Author	: Hans211
//	Date	: 4 April 2008
//
//	Credits : Game-Deception for their info and support
//	
//	Tools used:
//		Microsoft Visual Studio c++ 6.0
//		DirectX9 SDK Update (summer 2004)
//		D3Dfont.cpp
//
//	Features: 
//		Use Insert key to show/hide menu
//		Use up and down arrow keys to manouvre through the menuitems
//		Use left and right arrow keys to toggle menuitems
//		Items can have multiple custom states like: "Off" "On"  or "Head" "Neck" "Spine"
//		Support for textonly items
//		Support for folder style items
//
//  Changes:
//    1.2	Thx to DXT-Wieter20 for pointing this out
//			Check for left and right key first and then if value is within range
//
#include"D3dMenu1.h"
#include"D3dColor.h"
char Mtitle[81]="";	  // Some room for a title
int Mpos=0;			  // current highlighted menuitem	
int Mmax=0;			  // number of menu items
float Mxofs =120.0f;	  // offset for option text
float Mysize=13.0f;	  // heigh of a menuline
int Mvisible=1;
int YPOS;
// predifine some basic options
char*Moptfolder[]={"  +","  -"};	
char*Moptonoff[]={"OFF","ON"};

struct {
  int  typ;		  // type of menuline, folder, item
  char *txt;	  // text to show
  char **opt;	  // array of options
  int  *var;	  // variable containing current status
  int  maxvalue;  // maximumvalue,  normally 1  gives  0=off  1=on
} MENU[MENUMAXITEMS];

void MenuAddItem(char *txt, char **opt, int *var, int maxvalue, int typ){
  MENU[Mmax].typ=typ;
  MENU[Mmax].txt=txt;
  MENU[Mmax].opt=opt;
  MENU[Mmax].var=var;
  MENU[Mmax].maxvalue=maxvalue;
  Mmax++;}

void DrawCrosshair(LPDIRECT3DDEVICE8 pDevice, D3DCOLOR Color){
		int size = 8, strong = 1;
		int iCenterX = GetSystemMetrics( 0 ) / 2;
		int iCenterY = GetSystemMetrics( 1 ) / 2;
		if( iCenterX < 20 && iCenterY < 20 ){
			iCenterX = ( GetSystemMetrics( 0 ) / 2 );
			iCenterY = ( GetSystemMetrics( 1 ) / 2 );}
		D3DRECT rec_a = { iCenterX - size, iCenterY, iCenterX + size, iCenterY + strong};
		D3DRECT rec_b = { iCenterX, iCenterY - size, iCenterX + strong,iCenterY + size};
		pDevice->Clear(1, &rec_a, D3DCLEAR_TARGET, Color, 0,  0);
		pDevice->Clear(1, &rec_b, D3DCLEAR_TARGET, Color, 0,  0);}

void MenuShow(float x, float y,	CD3DFont *pFont1,CD3DFont *pFont2,LPDIRECT3DDEVICE8 pDevice){
  int i, val;
  DWORD color;

  if (!Mvisible) return;

  if (Mtitle[0]) {
	  pFont2->DrawText((x * 7) + 3, y-1, MENU_COLORTITLE, Mtitle,DT_CENTER|DT_SHADOW);
	  y+=(Mysize+8);}

  for (i=0; i<Mmax; i++) {
	   val=(MENU[i].var)?(*MENU[i].var):0;
       if (i==Mpos)
           color=MENU_COLORCURRENT;
       else if (MENU[i].typ==MENUFOLDER)
           color=MENU_COLORCAT;
       else if (MENU[i].typ==MENUTEXT)
           color=MENU_COLORTEXT;
       else
		   color=(val)?MCOLOR_ACTIVE:MCOLOR_INACTIVE;

	   if (MENU[i].opt){
		   if (MENU[i].typ==MENUFOLDER){
				pFont2->DrawText(x+3, y, color,MENU[i].txt,DT_SHADOW);
				y = y + 2;
		   }else{
				pFont1->DrawText(x+3, y, color,MENU[i].txt,DT_SHADOW);}}

       if (MENU[i].opt) {
		   if (MENU[i].typ==MENUTEXT)
			   pFont1->DrawText((x+Mxofs) + 15, y, color,(char *)MENU[i].opt,DT_RIGHT|DT_SHADOW);
		   else
			   pFont1->DrawText((x+Mxofs) + 15, y, color,(char *)MENU[i].opt[val],DT_RIGHT|DT_SHADOW);}

       y+=Mysize;}
  YPOS=int(y);}

void MenuNav(void){
  if (GetAsyncKeyState(VK_INSERT)&1) Mvisible=(!Mvisible);
  if (!Mvisible) return;

  if (GetAsyncKeyState(VK_UP)&1) {
		do {
			Mpos--;
			if (Mpos<0)  Mpos=Mmax-1;
		} while (MENU[Mpos].typ==MENUTEXT);		// skip textitems
  } else if (GetAsyncKeyState(VK_DOWN)&1) {
		do {
			Mpos++;
		    if (Mpos==Mmax) Mpos=0;
		} while (MENU[Mpos].typ==MENUTEXT);		// skip textitems
  } else if (MENU[Mpos].var) {
		int dir=0;

		if (GetAsyncKeyState(VK_LEFT )&1 && *MENU[Mpos].var > 0                      ) dir=-1;
		if (GetAsyncKeyState(VK_RIGHT)&1 && *MENU[Mpos].var < (MENU[Mpos].maxvalue-1)) dir=1;
		if (dir) {
			*MENU[Mpos].var += dir;
			if (MENU[Mpos].typ==MENUFOLDER) Mmax=0;
			if (MENU[Mpos].txt=="Chams") Mmax=0;}}}
