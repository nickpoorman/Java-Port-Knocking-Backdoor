#include "d3dmenu.h"
#include "addies.h"
#include "Color.h"
char        sFPS[20]="xxx fps";

//Folders
int			CT_d3d		= 0;
int			CT_player1  = 0;
int			CT_player2	= 0;
int			CT_weap		= 0;
int			CT_vehicle	= 0;
int			CT_server	= 0;
int			CT_px		= 0;

//Items Menu - D3D Related
int		CH_Chams		= 0;
int		CH_ColorC1		= 0;
int		CH_ColorC2		= 0;
int		CH_colorw		= 0;
int		CH_cross		= 0;
int		CH_wire			= 0;
int		CH_point		= 0;
int		CH_wall			= 0;
int		CH_bright		= 0;

//Items Menu - Player Related
int     CH_stamina		= 0;
int		CH_speed		= 0;
int		CH_jump			= 0;
int		CH_nfd			= 0;
int		CH_water		= 0;
int		CH_bounds		= 0;

//Items Menu - Weapon Related
int     CH_nospread		= 0;
int     CH_norecoil		= 0;

//Items Menu - Server Related
int		CH_slot			= 0;
int		CH_premium		= 0;

//Bools for Hacks
bool	SPEED			= true;
bool	SLOT			= true;

// Aditional Options
char *optColor1[]	= {"OFF","White","Red","Green","Blue","Black","Purple","Grey","Yellow","Orange"};
char *optColor2[]	= {"White","Red","Green","Blue","Black","Purple","Grey","Yellow","Orange"};
char *sStamina[]	= { "OFF","Stealth","Full" };
char *optMax[]		= { "OFF", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99"};
char *optJump[]		= {"OFF","500","1000","2000","4000"};
char *optPrem[]		= {"OFF","Gold","Silver","Bronze","Platinium"};

//Menu Builder
void RebuildMenu(void)
{
	strcpy_s(Mtitle,"[ AnaKrime Public  ]"); //Tittle

   MenuAddItem("[D3D]", Moptfolder, &CT_d3d, 2, MENUFOLDER); //Folder D3D

   if(CT_d3d){
	  MenuAddItem("Chams"		, Moptonoff , &CH_Chams	    , 2, MENUITEM);

	  if(CH_Chams > 0)
	  {
	  	  MenuAddItem("Color1"		, optColor2 , &CH_ColorC1	, 9, MENUITEM);
	  	  MenuAddItem("Color2"		, optColor2 , &CH_ColorC2	, 9, MENUITEM);
	  }

	  MenuAddItem("WallHack"	, Moptonoff , &CH_wall	    , 2, MENUITEM);
	  MenuAddItem("WallColor"	, optColor1 , &CH_colorw    , 10, MENUITEM);
	  MenuAddItem("PoitMode"	, Moptonoff , &CH_point	    , 2, MENUITEM);
	  MenuAddItem("WireFrame"	, Moptonoff , &CH_wire	    , 2, MENUITEM);
	  MenuAddItem("FullBright"	, Moptonoff	, &CH_bright    , 2, MENUITEM);
	  MenuAddItem("Crosshair"	, optColor1 , &CH_cross	    , 10, MENUITEM);
   }


  MenuAddItem("[Player]", Moptfolder, &CT_player1, 2, MENUFOLDER); //Folder Player
  if (CT_player1) {
	  MenuAddItem("Stamina"		, sStamina	, &CH_stamina	, 3, MENUITEM);
	  MenuAddItem("SuperJump"	, optJump	, &CH_jump		, 5, MENUITEM);
	  MenuAddItem("Speed"	    , optMax	, &CH_speed		, 100, MENUITEM);
      MenuAddItem("NoFallDamage", Moptonoff , &CH_nfd		, 2, MENUITEM);
	  MenuAddItem("NoBounds"	, Moptonoff , &CH_bounds		, 2, MENUITEM);

  }

  MenuAddItem("[Weapons]", Moptfolder, &CT_weap, 2, MENUFOLDER); //Folder Weapon
  if (CT_weap) {
      MenuAddItem("NoSpread"		, Moptonoff , &CH_nospread	, 2, MENUITEM);
      MenuAddItem("NoRecoil"		, Moptonoff , &CH_norecoil	, 2, MENUITEM);
  }

  MenuAddItem("[Server]", Moptfolder, &CT_server, 2, MENUFOLDER); //Folder Server
  if (CT_server) {
	  MenuAddItem("5-Slot"		, Moptonoff , &CH_slot		  , 2, MENUITEM);
	  MenuAddItem("Premium"		, optPrem	, &CH_premium	  , 5, MENUITEM);
  }

}

//// ------------------------- END OF MENU ------------------------- /////

//// ------------------------- START HACKS ------------------------- /////

void WriteAsm(void* pxAddress, BYTE *code, int size)
{
	DWORD Protection;
	VirtualProtect((void*)pxAddress, size, PAGE_READWRITE, &Protection);
	memcpy((void*)pxAddress, (const void*)code, size);
	VirtualProtect((void*)pxAddress, size, Protection, 0);
}


//Hacks in Game, Work Only if u are Playing in a Room

void HacksInGame()
{
	DWORD dwPlayerPointer = *(DWORD*)ADR_PLAYERBASE;
	if(dwPlayerPointer != 0) //If is != of 0 u are in a Room.
	{
		//======= Stamina Hack =======
		switch(CH_stamina) 
		{
			case 1: //Steal Stamina
				if(*(float*)(dwPlayerPointer+OFS_STAMINA) < 45.0f) 
				{
					*(float*)(dwPlayerPointer+OFS_STAMINA) = 45;
				}
			break;

			case 2: //Full Stamina
				*(float*)(dwPlayerPointer+OFS_STAMINA) = 100;
			break;	
		}
		
		//======= Speed Hack ======= 
		if(CH_speed == 0){
			if(!SPEED){
				*(float*)(ADR_SPEED)=96;
				SPEED = true;
			}
		}

		if(CH_speed >= 1){
			*(float*)(ADR_SPEED) = float(96 * CH_speed);
			SPEED = false;
		}

		
		//======= NoFallDamage Hack ======= 
		if(CH_nfd==1)
		{
			*(float*)(dwPlayerPointer+OFS_NOFALL) = -2000;
		}

		//======= SuperJump Hack =======
		switch(CH_jump) {
			case 1:
				if(GetAsyncKeyState(VK_CONTROL)) {*(float*)(dwPlayerPointer + OFS_SUPERJUMP) = 500;}
				break;
			case 2:
				if(GetAsyncKeyState(VK_CONTROL)) {*(float*)(dwPlayerPointer + OFS_SUPERJUMP) = 1000;}
				break;
			case 3:
				if(GetAsyncKeyState(VK_CONTROL)) {*(float*)(dwPlayerPointer + OFS_SUPERJUMP) = 2000;}
				break;
			case 4:
				if(GetAsyncKeyState(VK_CONTROL)) {*(float*)(dwPlayerPointer + OFS_SUPERJUMP) = 4000;}
				break;
		}
		
		//======= NoBounds Hack =======
		if(CH_bounds==1) {
			*(int*)(ADR_NOBOUNDS1) = 0;
			*(int*)(ADR_NOBOUNDS2) = 0;
		}

		//======= No Recoil Hack =======
		if(CH_norecoil==1) 
		{
			*(float*)(dwPlayerPointer+OFS_RECOIL1) = 0;
			*(float*)(dwPlayerPointer+OFS_RECOIL2) = 0;
			*(float*)(dwPlayerPointer+OFS_RECOIL3) = 0;
		}

		//======= No Spread Hack =======
		if(CH_nospread==1)
		{
			*(int*)ADR_NOSPREAD = 0;
		}

	}// End of Check InGame

}//End of Hacks InGame


//Hacks in Server, Only Work if are Logged on the Server
void HacksInServer()
{
	DWORD dwServerPointer = *(DWORD*)ADR_SERVERBASE;
	if(dwServerPointer != 0)//If is != of 0 u are Logged on the Server
	{
		//======= Premium Hack ======
		switch(CH_premium)
		{
		case 1:
			*(int*)(dwServerPointer+OFS_PREMIUM) = 1;
			break;
		case 2:
			*(int*)(dwServerPointer+OFS_PREMIUM) = 2;
			break;
		case 3:
			*(int*)(dwServerPointer+OFS_PREMIUM) = 3;
			break;
		case 4:
			*(int*)(dwServerPointer+OFS_PREMIUM) = 4;
			break;
		}

		//======= Slots Hack ======
		if(CH_slot==1)
		{
			if(SLOT)
			{
				*(int*)(dwServerPointer + OFS_UNLOCK5SLOT) = 1;
				SLOT = false;
			}
		}
		else
		{	
			if(!SLOT)
			{
				*(int*)(dwServerPointer + OFS_UNLOCK5SLOT) = 0;
				SLOT = true;
			}
		}


	}//End of Check in Server

}//End of Hack in Server
