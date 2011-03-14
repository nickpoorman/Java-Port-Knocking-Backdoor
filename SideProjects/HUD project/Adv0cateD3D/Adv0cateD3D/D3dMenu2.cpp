/*Anakrime Put This Together I Changed Like 10% OF It SO Credits TO: ANAKRIME & Hans211 Brought 2 You By Adv0cate :)*/
#include "D3dMenu1.h"
#include "Addresses.h"
#include "D3dColor.h"
#include <Stdio.h>
//Folders
int CT_Visual=1;
int CT_Player1=1;
int CT_Player2=1;
int CT_Weapon=1;
int CT_Server=1;
int CT_Gps=1;
int CT_Px=1;
//Items Menu - D3D Related
int CH_Chams=0;
int CH_ColorC1=0;
int CH_ColorC2=0;
int CH_ColorW=0;
int CH_Cross=0;
int CH_Wire=0;
int CH_Point=0;
int CH_Wall=0;
int CH_Bright=0;
//Bools for Hacks
//bool ?????=true;
// Aditional Options
char*optColor1[]={"OFF","White","Red","Green","Blue","Black","Purple","Grey","Yellow","Orange"};
char*optColor2[]={"White","Red","Green","Blue","Black","Purple","Grey","Yellow","Orange"};
//Menu Builder
void RebuildMenu(void){
	strcpy_s(Mtitle,"  Title");//Tittle
   MenuAddItem("  Visual",Moptfolder,&CT_Visual,2,MENUFOLDER);//Folder D3D
   if(CT_Visual){
	  MenuAddItem("Chams",Moptonoff,&CH_Chams,2,MENUITEM);
	  if(CH_Chams>0){
	  	  MenuAddItem("Color1",optColor2,&CH_ColorC1,9,MENUITEM);
	  	  MenuAddItem("Color2",optColor2,&CH_ColorC2,9,MENUITEM);}
	  MenuAddItem("Wall Hack",Moptonoff,&CH_Wall,2,MENUITEM);
	  MenuAddItem("Cross-Hair",optColor1,&CH_Cross,10,MENUITEM);
	  MenuAddItem("Full Bright",Moptonoff,&CH_Bright,2,MENUITEM);
	  MenuAddItem("Wall Color",optColor1,&CH_ColorW,10,MENUITEM);
	  MenuAddItem("Point Mode",Moptonoff,&CH_Point,2,MENUITEM);
	  MenuAddItem("Wire Frame",Moptonoff,&CH_Wire,2,MENUITEM);}
   MenuAddItem("  Gps",Moptfolder,&CT_Gps,2,MENUFOLDER);//Folder Gps
   if(CT_Gps){}
  MenuAddItem("  Player",Moptfolder,&CT_Player1,2,MENUFOLDER);//Folder Player
  if(CT_Player1){}
  MenuAddItem("  Px Items",Moptfolder,&CT_Px,2,MENUFOLDER);//Folder Px
  if(CT_Px){}
  MenuAddItem("  Weapon",Moptfolder,&CT_Weapon,2,MENUFOLDER);//Folder Weapon
  if(CT_Weapon){}
  MenuAddItem("  Server",Moptfolder,&CT_Server,2,MENUFOLDER);//Folder Server
  if(CT_Server){
  
  }

}

//// ------------------------- END OF MENU ------------------------- /////

//// ------------------------- START HACKS ------------------------- /////

//Hacks In Game, Work Only If U Are Playing In A Room
void HacksInGame(){
	DWORD dwPlayerPointer=*(DWORD*)ADR_PLAYERBASE;
	if(dwPlayerPointer!=0){ //If is != of 0 u are in a Room.


	}// End of Check InGame

}//End of Hacks InGame


//Hacks InServer, Only Work If Are Logged On The Server
void HacksInServer(){
	DWORD dwServerPointer=*(DWORD*)ADR_SERVERBASE;
	if(dwServerPointer != 0){//If is != of 0 u are Logged on the Server


	}//End of Check InServer

}//End of Hack InServer

//AsmIn InServer & InGame, Only Work If Are Logged On The Server & Work Only If U Are Playing In A Room
void AsmIn(){
	DWORD dwPlayerPointer=*(DWORD*)ADR_PLAYERBASE;
	if(dwPlayerPointer!=0){
	DWORD dwServerPointer=*(DWORD*)ADR_SERVERBASE;
	if(dwServerPointer!=0){

		}// End of Check InGame
	}//End of Check InServer

}//End of AsmIn

void CmdIn(){

}

void AntiBan(void){
DWORD Old_Protection;
VirtualProtect((BYTE*)int(ADR_ANTIBAN),0x8,PAGE_EXECUTE_READWRITE,&Old_Protection);
*(int*)ADR_ANTIBAN=2415922370;}

void EndProcess(){
if(GetAsyncKeyState(VK_F12)){
ExitProcess(0);}}
