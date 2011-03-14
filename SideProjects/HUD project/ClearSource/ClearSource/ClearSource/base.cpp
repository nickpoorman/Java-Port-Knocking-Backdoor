//Includes
#include <windows.h>
#include <stdio.h>
#include <d3d8.h>
#include <d3dx8.h>
#include <fstream>
#include "color.h"

/*
Detours Removeds!! I Dont need any other "Coder" using it
If u have it Can Test it and if not, Try Learn C++ or make c&p from other
site and still believing u are Coder :P

AnaKrime.

#include "detour.h"
*/

#include "base.h"
#include "d3dmenu.h"
#include "Menu.h"
#include "Structs.h"

//Font Definitioms
CD3DFont	*pFont1;
CD3DFont	*pFont2;

//D3D8 Definitios
IDirect3DDevice8 *m_pDevice = 0;
LPDIRECT3DDEVICE8 pDevice = 0;
LPDIRECT3DTEXTURE8 White,Red,Green,Blue,Black,Purple,Grey,Yellow,Orange;
UINT m_Stride;

//BOOL for Initialize Textures
bool D3Dactive = false;


//---------------------------- Generate Textures ---------------------------------
HRESULT GenerateTexture(IDirect3DDevice8 *pD3Ddev, IDirect3DTexture8 **ppD3Dtex, DWORD colour32)
{
    if(FAILED(pD3Ddev->CreateTexture(8, 8, 1, 0, D3DFMT_A4R4G4B4, D3DPOOL_MANAGED, ppD3Dtex)))
        return E_FAIL;
    
    WORD colour16 = ((WORD)((colour32>>28)&0xF)<<12)
	            	|(WORD)(((colour32>>20)&0xF)<<8)
	             	|(WORD)(((colour32>>12)&0xF)<<4)
                 	|(WORD)(((colour32>>4)&0xF)<<0);

    D3DLOCKED_RECT d3dlr;    
    (*ppD3Dtex)->LockRect(0, &d3dlr, 0, 0);
    WORD *pDst16 = (WORD*)d3dlr.pBits;

    for(int xy=0; xy < 8*8; xy++)
        *pDst16++ = colour16;

    (*ppD3Dtex)->UnlockRect(0);

    return S_OK;
}


//---------- Initialize Textures -------------
HRESULT InitializeTextures()
{
	GenerateTexture(m_pDevice, &White,  WHITE2);
	GenerateTexture(m_pDevice, &Red,    RED2);
	GenerateTexture(m_pDevice, &Green,  GREEN2);
	GenerateTexture(m_pDevice, &Blue,   BLUE2);
	GenerateTexture(m_pDevice, &Black,  BLACK2);
	GenerateTexture(m_pDevice, &Purple, PURPLE2);
	GenerateTexture(m_pDevice, &Grey,   GREY2);
	GenerateTexture(m_pDevice, &Yellow, YELLOW2);
	GenerateTexture(m_pDevice, &Orange, ORANGE2);

	return S_OK;
}

//----------- Crosshair Void -----------

void CrossHair(LPDIRECT3DDEVICE8 pDevice){

	switch(CH_cross) 
	{
		case 1: DrawCrosshair(pDevice, WHITE); break;
		case 2: DrawCrosshair(pDevice, RED); break;			
		case 3: DrawCrosshair(pDevice, GREEN); break;
		case 4: DrawCrosshair(pDevice, BLUE); break;
		case 5: DrawCrosshair(pDevice, BLACK); break;
		case 6: DrawCrosshair(pDevice, PURPLE); break;
		case 7: DrawCrosshair(pDevice, GREY); break;
		case 8: DrawCrosshair(pDevice, YELLOW); break;
		case 9: DrawCrosshair(pDevice, ORANGE); break;
	}

}

HRESULT PreReset()
{	
	if (pFont1) {
		pFont1->InvalidateDeviceObjects();
		pFont1->DeleteDeviceObjects();
		pFont1 = NULL;
	}

	if (pFont2) {
		pFont2->InvalidateDeviceObjects();
		pFont2->DeleteDeviceObjects();
		pFont2 = NULL;
	}
	
	return S_OK;
}

HRESULT PostReset(LPDIRECT3DDEVICE8 pDevice)
{
	pFont1 = new CD3DFont("Arial", 8);
	pFont2 = new CD3DFont("Arial", 10);

	if (pFont1) {
		pFont1->InitDeviceObjects(pDevice);
		pFont1->RestoreDeviceObjects();
	}

	if (pFont2) {
		pFont2->InitDeviceObjects(pDevice);
		pFont2->RestoreDeviceObjects();
	}

	return S_OK;
}

HRESULT WINAPI myDrawIndexedPrimitive(LPDIRECT3DDEVICE8 pDevice, D3DPRIMITIVETYPE pType, UINT nMinIndex, UINT nNumVertices, UINT nStartIndex, UINT nPrimitiveCount)
{
	_asm pushad;

	DWORD dwOldZEnable = D3DZB_TRUE;

	
if(CH_Chams){
	if(m_Stride == 44){
	pDevice->SetRenderState(D3DRS_CLIPPING,FALSE);
	pDevice->GetRenderState(D3DRS_ZENABLE, &dwOldZEnable);
	pDevice->SetRenderState(D3DRS_ZENABLE, D3DZB_FALSE);
		if(CH_ColorC1==0) pDevice->SetTexture(0, White);
		if(CH_ColorC1==1) pDevice->SetTexture(0, Red);
		if(CH_ColorC1==2) pDevice->SetTexture(0, Green);
		if(CH_ColorC1==3) pDevice->SetTexture(0, Blue);
		if(CH_ColorC1==4) pDevice->SetTexture(0, Black);
		if(CH_ColorC1==5) pDevice->SetTexture(0, Purple);
		if(CH_ColorC1==6) pDevice->SetTexture(0, Grey);
		if(CH_ColorC1==7) pDevice->SetTexture(0, Yellow);		
		if(CH_ColorC1==8) pDevice->SetTexture(0, Orange);
	pDrawIndexedPrimitive(pDevice, pType, nMinIndex, nNumVertices, nStartIndex, nPrimitiveCount);
	pDevice->SetRenderState(D3DRS_CLIPPING,FALSE);
	pDevice->GetRenderState(D3DRS_ZENABLE, &dwOldZEnable);
	pDevice->SetRenderState(D3DRS_ZENABLE, D3DZB_TRUE);
		if(CH_ColorC2==0) pDevice->SetTexture(0, White);
		if(CH_ColorC2==1) pDevice->SetTexture(0, Red);
		if(CH_ColorC2==2) pDevice->SetTexture(0, Green);
		if(CH_ColorC2==3) pDevice->SetTexture(0, Blue);
		if(CH_ColorC2==4) pDevice->SetTexture(0, Black);
		if(CH_ColorC2==5) pDevice->SetTexture(0, Purple);
		if(CH_ColorC2==6) pDevice->SetTexture(0, Grey);
		if(CH_ColorC2==7) pDevice->SetTexture(0, Yellow);		
		if(CH_ColorC2==8) pDevice->SetTexture(0, Orange);
	}
}

		if((CH_colorw>0)&&(m_Stride == 40)) {
		pDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, D3DZB_TRUE);
		pDevice->SetRenderState(D3DRS_DESTBLEND,D3DBLEND_INVSRCALPHA);
		if(CH_colorw==1) pDevice->SetTexture(0, White);
		if(CH_colorw==2) pDevice->SetTexture(0, Red);
		if(CH_colorw==3) pDevice->SetTexture(0, Green);
		if(CH_colorw==4) pDevice->SetTexture(0, Blue);
		if(CH_colorw==5) pDevice->SetTexture(0, Black);
		if(CH_colorw==6) pDevice->SetTexture(0, Purple);
		if(CH_colorw==7) pDevice->SetTexture(0, Grey);
		if(CH_colorw==8) pDevice->SetTexture(0, Yellow);		
		if(CH_colorw==9) pDevice->SetTexture(0, Orange);
		}

		if((CH_wall) && (m_Stride == 44)){
			pDevice->SetRenderState(D3DRS_ZENABLE,D3DZB_FALSE);
		}

if (CH_wire==1){
	if(m_Stride == 44){
		pDevice->SetRenderState(D3DRS_FILLMODE,D3DFILL_WIREFRAME);
	}
}

if(CH_point==1){
	if(m_Stride == 44){
		pDevice->SetRenderState(D3DRS_FILLMODE,D3DFILL_POINT);
	}
}


if(CH_bright==1) 
{ 
pDevice->SetRenderState(D3DRS_LIGHTING, false); 
}


	_asm popad;	
	return pDrawIndexedPrimitive(pDevice, pType, nMinIndex, nNumVertices, nStartIndex, nPrimitiveCount);
}

HRESULT WINAPI mySetStreamSource(LPDIRECT3DDEVICE8 pDevice, UINT nStreamNumber, LPDIRECT3DVERTEXBUFFER8 pStreamData, UINT nStride)
{
	_asm pushad;

	if (nStreamNumber==0) m_Stride = nStride;

	_asm popad;
	return pSetStreamSource(pDevice, nStreamNumber, pStreamData, nStride);
}

HRESULT WINAPI myPresent(LPDIRECT3DDEVICE8 pDevice, CONST RECT* pSourceRect,CONST RECT* pDestRect, HWND hDestWindowOverride,CONST RGNDATA* pDirtyRegion)
{
	_asm pushad;
	
	//--------- Initialize Textures ---------
	if(m_pDevice==0) {m_pDevice = pDevice;}	

	//--------- Initialize Fonts ------------
	PostReset(pDevice);

	//--------- Rebuld Menu -------------
	if (Mmax==0) RebuildMenu();
    MenuShow(12,10,pFont1,pFont2,pDevice);
	MenuNav();

	//--------- Initialize Crosshairs ----------
	CrossHair(pDevice);

	//--------- Initialize Hacks --------- 
	HacksInGame();
	HacksInServer();

	//--------- Destroy Fonts ---------
	PreReset();


	_asm popad;	
	return pPresent(pDevice, pSourceRect, pDestRect, hDestWindowOverride, pDirtyRegion);
}

int D3D (void)
{
	HMODULE CBBase = NULL;
	for (;CBBase == NULL;Sleep(100))
	CBBase = LoadLibrary("d3d8.dll");
	DWORD* VTableS = 0;                  
	DWORD hD3D8 = (DWORD)GetModuleHandle("d3d8.dll");
	DWORD table = FindPattern(hD3D8, 0x128000, (PBYTE)"\xC7\x06\x00\x00\x00\x00\x89\x86\x00\x00\x00\x00\x89\x86", "xx????xx????xx");
	memcpy(&VTableS, (void*)(table+2), 4);

	DWORD dwDrawIndexedPrimitive    = VTableS[71];
	DWORD dwSetStreamSource		    = VTableS[83];
	DWORD dwPresent				    = VTableS[15];

	int hook = 7; 

	pDrawIndexedPrimitive = (oDrawIndexedPrimitive)DetourCreateJMP((PBYTE)dwDrawIndexedPrimitive, (PBYTE)myDrawIndexedPrimitive, hook);
	pSetStreamSource      = (oSetStreamSource)     DetourCreateJMP((PBYTE)dwSetStreamSource,      (PBYTE)mySetStreamSource,      hook);
	pPresent			  = (oPresent)			   DetourCreateJZ((PBYTE)VTableS[15],			  (PBYTE)myPresent,		DETOUR_TYPE_PUSH_RET,	6);
	
	if(m_pDevice==0) { 
          do { 
			  Sleep(5);
		  } while(m_pDevice==0);
		  InitializeTextures();		  
		  D3Dactive = true;
	}


	return 0;
}

void pHS(DWORD dwAddr)
{
	DWORD flOldProtect;
	VirtualProtect((void *)dwAddr, 0x6, PAGE_READWRITE, &flOldProtect);
	*(DWORD*)dwAddr = 4;
	VirtualProtect((void *)dwAddr, 0x6, flOldProtect, 0);
}

void cpMds( void ) 
{
	DWORD hEhSvc;
	for(;;) {
		hEhSvc = (DWORD)GetModuleHandle("EhSvc.dll");
		if(hEhSvc != 0) {

			pHS(hEhSvc+0xC7758-0x20);
			pHS(hEhSvc+0xC7758-0x40);
			pHS(hEhSvc+0xC7758-0x44);

		}
		Sleep(25);
	}
}


BOOL WINAPI DllMain(HMODULE hDll, DWORD dwReason, LPVOID lpReserved){
{
 switch (dwReason)
 {
	case DLL_PROCESS_ATTACH:

		DisableThreadLibraryCalls(hDll);
		HANDLE hThread1 = CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)cpMds, NULL, NULL, NULL);
		CloseHandle(hThread1);
		HANDLE hThread2 = CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)D3D, NULL, NULL, NULL);
		CloseHandle(hThread2);

	break;
	}
return TRUE;
}
}