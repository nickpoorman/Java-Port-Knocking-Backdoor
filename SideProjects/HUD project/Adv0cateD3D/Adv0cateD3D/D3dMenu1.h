//	Project : Basic D3D Menu v1.1
//	Author	: Hans211
//	Date	: 4 April 2008
/*Anakrime + Adv0cate + Hans211 + Brom + ATX Coder + Gordon + Croner + Warlord*/
#ifndef _D3DMENU_H
#define _D3DMENU_H

#define WIN32_LEAN_AND_MEAN
#include<Windows.h>

#include"D3dFont.h"
#ifndef D3DFONT_RIGHT
#define D3DFONT_RIGHT		0x0008
#endif

#define MENUMAXITEMS	100

#define MENUFOLDER		1
#define MENUTEXT		2
#define MENUITEM		3

#define MCOLOR_INACTIVE	0xFFA0A0A0
#define MCOLOR_ACTIVE	0xFFFFFFFF

extern int Mpos;				// current highlighted menuitem	
extern int Mmax;				// number of menu items
extern float Mxofs;				// offset for option text
extern float Mysize;				// heigh of a menuline
extern char Mtitle[81];			// some menu title
extern int Mvisible;

extern char*Moptfolder[];		// "+"  , "-"
extern char*Moptonoff[];		// "Off", "On"
extern int YPOS;

void MenuAddItem(char *txt, char **opt, int *var, int maxvalue, int typ);
void MenuShow(float x, float y,	CD3DFont *pFont1,CD3DFont *pFont2,LPDIRECT3DDEVICE8 pDevice);
void DrawMenu(int x, int y, int w, int h, int px,D3DCOLOR col1,D3DCOLOR col2, LPDIRECT3DDEVICE8 pDevice);
void DrawCursor( int x, int y, int w, int h, D3DCOLOR Color,LPDIRECT3DDEVICE8 pDevice);
void Imprimir(float x, float y, char *texto, CD3DFont *pFont1);
void DrawCrosshair(LPDIRECT3DDEVICE8 pDevice, D3DCOLOR Color);
void MenuNav(void);
#endif
