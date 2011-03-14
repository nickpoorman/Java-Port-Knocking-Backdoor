/*Anakrime Put This Together I Changed Like 10% OF It SO Credits TO: ANAKRIME & Hans211 Brought 2 You By Adv0cate :)*/
//Includes
#include<Windows.h>
#include<Stdio.h>
#include<D3d8.h>
#include<D3dx8.h>
#include<Fstream>
#include"D3dColor.h"
#include"D3dDetour.h"
#include"D3dBase.h"
#include"D3dMenu1.h"
#include"D3DMenu2.h"

//Font Definitioms
CD3DFont*pFont1;
CD3DFont*pFont2;

//D3D8 Definitios
IDirect3DDevice8*m_pDevice=0;
LPDIRECT3DDEVICE8 pDevice=0;
LPDIRECT3DTEXTURE8 White,Red,Green,Blue,Black,Purple,Grey,Yellow,Orange;
UINT m_Stride;

//BOOL for Initialize Textures
bool D3DActive=false;

//---------------------------- Generate Textures ---------------------------------
HRESULT GenerateTexture(IDirect3DDevice8 *pD3Ddev, IDirect3DTexture8 **ppD3Dtex, DWORD colour32){
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

    return S_OK;}

//---------- Initialize Textures -------------
HRESULT InitializeTextures(){
	GenerateTexture(m_pDevice,&White,  WHITE2);
	GenerateTexture(m_pDevice,&Red,    RED2);
	GenerateTexture(m_pDevice,&Green,  GREEN2);
	GenerateTexture(m_pDevice,&Blue,   BLUE2);
	GenerateTexture(m_pDevice,&Black,  BLACK2);
	GenerateTexture(m_pDevice,&Purple, PURPLE2);
	GenerateTexture(m_pDevice,&Grey,   GREY2);
	GenerateTexture(m_pDevice,&Yellow, YELLOW2);
	GenerateTexture(m_pDevice,&Orange, ORANGE2);

	return S_OK;}

//----------- Crosshair Void -----------

void CrossHair(LPDIRECT3DDEVICE8 pDevice){
	switch(CH_Cross){
		case 1: DrawCrosshair(pDevice,WHITE);break;
		case 2: DrawCrosshair(pDevice,RED);break;			
		case 3: DrawCrosshair(pDevice,GREEN);break;
		case 4: DrawCrosshair(pDevice,BLUE);break;
		case 5: DrawCrosshair(pDevice,BLACK);break;
		case 6: DrawCrosshair(pDevice,PURPLE);break;
		case 7: DrawCrosshair(pDevice,GREY);break;
		case 8: DrawCrosshair(pDevice,YELLOW);break;
		case 9: DrawCrosshair(pDevice,ORANGE);break;}}

HRESULT PreReset(){	
	if(pFont1){
		pFont1->InvalidateDeviceObjects();
		pFont1->DeleteDeviceObjects();
		pFont1 = NULL;}

	if(pFont2){
		pFont2->InvalidateDeviceObjects();
		pFont2->DeleteDeviceObjects();
		pFont2 = NULL;}
	
	return S_OK;}

HRESULT PostReset(LPDIRECT3DDEVICE8 pDevice){
	pFont1=new CD3DFont("Arial",8);
	pFont2=new CD3DFont("Arial",9);

	if(pFont1){
		pFont1->InitDeviceObjects(pDevice);
		pFont1->RestoreDeviceObjects();}

	if(pFont2){
		pFont2->InitDeviceObjects(pDevice);
		pFont2->RestoreDeviceObjects();}

	return S_OK;}

HRESULT WINAPI myDrawIndexedPrimitive(LPDIRECT3DDEVICE8 pDevice, D3DPRIMITIVETYPE pType, UINT nMinIndex, UINT nNumVertices, UINT nStartIndex, UINT nPrimitiveCount){
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
		if(CH_ColorC2==8) pDevice->SetTexture(0, Orange);}}

		if((CH_ColorW>0)&&(m_Stride == 40)) {
		pDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, D3DZB_TRUE);
		pDevice->SetRenderState(D3DRS_DESTBLEND,D3DBLEND_INVSRCALPHA);
		if(CH_ColorW==1) pDevice->SetTexture(0, White);
		if(CH_ColorW==2) pDevice->SetTexture(0, Red);
		if(CH_ColorW==3) pDevice->SetTexture(0, Green);
		if(CH_ColorW==4) pDevice->SetTexture(0, Blue);
		if(CH_ColorW==5) pDevice->SetTexture(0, Black);
		if(CH_ColorW==6) pDevice->SetTexture(0, Purple);
		if(CH_ColorW==7) pDevice->SetTexture(0, Grey);
		if(CH_ColorW==8) pDevice->SetTexture(0, Yellow);		
		if(CH_ColorW==9) pDevice->SetTexture(0, Orange);}

		if((CH_Wall) && (m_Stride == 44)){
			pDevice->SetRenderState(D3DRS_ZENABLE,D3DZB_FALSE);}

if (CH_Wire==1){
	if(m_Stride == 44){
		pDevice->SetRenderState(D3DRS_FILLMODE,D3DFILL_WIREFRAME);}}

if(CH_Point==1){
	if(m_Stride == 44){
		pDevice->SetRenderState(D3DRS_FILLMODE,D3DFILL_POINT);}}


if(CH_Bright==1){ 
		pDevice->SetRenderState(D3DRS_LIGHTING, false); }


	_asm popad;	
	return pDrawIndexedPrimitive(pDevice, pType, nMinIndex, nNumVertices, nStartIndex, nPrimitiveCount);}

HRESULT WINAPI mySetStreamSource(LPDIRECT3DDEVICE8 pDevice, UINT nStreamNumber, LPDIRECT3DVERTEXBUFFER8 pStreamData, UINT nStride){
	_asm pushad;

	if(nStreamNumber==0)m_Stride=nStride;

	_asm popad;
	return pSetStreamSource(pDevice, nStreamNumber, pStreamData, nStride);}

HRESULT WINAPI myPresent(LPDIRECT3DDEVICE8 pDevice, CONST RECT* pSourceRect,CONST RECT* pDestRect, HWND hDestWindowOverride,CONST RGNDATA* pDirtyRegion){
	_asm pushad;

	//--------- Initialize Textures ---------
	if(m_pDevice==0) {m_pDevice = pDevice;}	
	//--------- Initialize Fonts ------------
	PostReset(pDevice);
	//--------- Rebuld Menu -------------
	if (Mmax==0) RebuildMenu();
    MenuShow(10,10,pFont1,pFont2,pDevice);
	MenuNav();
	//--------- Initialize Crosshairs ----------
	CrossHair(pDevice);
	//--------- Initialize AntiBan ---------
	AntiBan();
	//--------- Initialize EndProcess --------- HotKey F12
	EndProcess();
	//--------- Initialize Hacks ---------
	HacksInGame();
	HacksInServer();
	AsmIn();
	CmdIn();
	//--------- Destroy Fonts ---------
	PreReset();

	_asm popad;	
	return pPresent(pDevice, pSourceRect, pDestRect, hDestWindowOverride, pDirtyRegion);}

bool bCompare(const BYTE*pData,const BYTE*bMask,const char*szMask){
	for(;*szMask;++szMask,++pData,++bMask)
		if(*szMask=='x' && *pData!=*bMask)   return 0;
	return (*szMask) == NULL;}

DWORD FindPattern(DWORD dwAddress,DWORD dwLen,BYTE*bMask,char*szMask){
	for(DWORD i=0; i<dwLen; i++)
		if (bCompare((BYTE*)(dwAddress+i),bMask,szMask))  return (DWORD)(dwAddress+i);
	return 0;}

int BaseModule(void){
	HMODULE CBBase=NULL;
	for(;CBBase==NULL;Sleep(100))
	CBBase=LoadLibrary("d3d8.dll");
	DWORD*VTableS=0;                  
	DWORD hD3D8=(DWORD)GetModuleHandle("d3d8.dll");
	DWORD table=FindPattern(hD3D8,0x128000,(PBYTE)"\xC7\x06\x00\x00\x00\x00\x89\x86\x00\x00\x00\x00\x89\x86","xx????xx????xx");
	memcpy(&VTableS,(void*)(table+2),4);

	DWORD dwDrawIndexedPrimitive=VTableS[71];
	DWORD dwSetStreamSource		=VTableS[83];
	DWORD dwPresent				=VTableS[15];

	int Hook=5; 
/*
	pDrawIndexedPrimitive=(oDrawIndexedPrimitive)Detour((PBYTE)dwDrawIndexedPrimitive,(PBYTE)myDrawIndexedPrimitive,Hook);
	pSetStreamSource     =(oSetStreamSource)     Detour((PBYTE)dwSetStreamSource,     (PBYTE)mySetStreamSource,Hook);
	pPresent			 =(oPresent)			 Detour((PBYTE)dwPresent,				(PBYTE)myPresent,Hook);
*/
	if(m_pDevice==0){do{ 
			Sleep(10);
		  }while(m_pDevice==0);
		  InitializeTextures();		  
		  D3DActive=true;}

	return 0;}

int BypassModule;//REMOVE N ADD A BYPASS

BOOL WINAPI DllMain(HMODULE hDll,DWORD dwReason,LPVOID lpReserved){{
 switch(dwReason){
	case DLL_PROCESS_ATTACH:

		DisableThreadLibraryCalls(hDll);
		HANDLE hThread1=CreateThread(NULL,NULL,(LPTHREAD_START_ROUTINE)BypassModule,NULL,NULL,NULL);
		CloseHandle(hThread1);
		HANDLE hThread2=CreateThread(NULL,NULL,(LPTHREAD_START_ROUTINE)BaseModule,NULL,NULL,NULL);
		CloseHandle(hThread2);

	break;}
return TRUE;}}