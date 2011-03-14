--[[
Worst script I ever contributed to, listen servers make checks hard... :|
All this is based on the logic that "Client" is in singleplayer, or is the listen server host, and, from a joiners point of view, the server is the dedicated server or the listenserver host.
Finally, it is also based on the logic that this script is either downloaded from a server, or is present in an autorun directory and is autoran on the client.
When some checks don't seem to make ANY sense, remember the above.
Yeah, now you can see why it was hard. -- Anticept
]]

if SERVER then
	AddCSLuaFile("wiremod_installed_improperly_popup.lua")
	
	if not SinglePlayer() then
		-- print output for SRCDS console
		if isDedicatedServer() then
			print([[-----Bad Wiremod Install-----
Your Server installation is incorrect.
Please review the web page located in your wire install folder called: 
"How To Install.txt"
-----------------------------]])
		end -- if isDedicatedServer
		
		hook.Add("PlayerInitialSpawn", "wiremod_installed_improperly_popup", function(ply)
			-- Send a umsg to tell joining clients that server does not have wiremod installed properly, and whether he's the host.
			umsg.Start("wire_server_bad_install", ply)
				umsg.Bool(ply:IsListenServerHost())
			umsg.End()
		end) -- PlayerInitialSpawn
	end -- if not SinglePlayer
end

-- library stuff, WBI = WireBadInstall
if CLIENT then
	local function PrintWBI(text)
		local fontnames = {
			"Trebuchet24",
			"Trebuchet22",
			"Trebuchet20",
			"Trebuchet19",
			"Trebuchet18",
		}
		
		hook.Add("HUDPaint", "wiremod_installed_improperly_popup", function()
			local fontname,w,h
			
			-- Find a font that fits the screen
			local fontindex = 0
			repeat
				fontindex = fontindex + 1
				fontname = fontnames[fontindex]
				surface.SetFont(fontname)
				w,h = surface.GetTextSize(text)
			until w+20 < ScrW() or fontindex == #fontnames
			
			-- draw the text centered on the screen
			local x,y = ScrW()/2-w/2, ScrH()/2-h/2
			
			-- on a grey box with black borders
			draw.RoundedBox(1, x-11, y-7, w+22, h+14, Color(0,0,0,192) )
			draw.RoundedBox(1, x-9, y-5, w+18, h+10, Color(128,128,128,255) )
			
			-- draw the text
			draw.DrawText(text, fontname, x, y, Color(255,255,255,255), TEXT_ALIGN_LEFT)
		end)
		
		print(text)
	end -- function PrintWBI
	
	local function clienthint(isListenHost, WireServerBadInstall)
		usermessage.Hook("wire_server_bad_install", function(um)
			clienthint(um:ReadBool(), true)
		end)

		-- default, blame client, for single player purposes
		local hint = [[
Your client's copy of wiremod is not installed properly. Please review the following tutorial:
http://www.wiremod.com/forum/wiremod-general-chat/4-wiremod-svn-guide.html
You can find a copy of this of this link in your wiremod install named "How To Install.txt"]]

		-- case for joining clients
		if not SinglePlayer() and not isListenHost then

			-- Did our client also correctly install wire?
			local function findInAddons(fn)
				for k,v in ipairs(file.FindDir("../addons/*")) do
					if file.Exists("../addons/" .. v .. "/" .. fn) then
						return true
					end
				end
				return false
			end -- findInAddons
			WireClientBadInstall = not findInAddons("lua/autorun/Wire_Load.lua") or findInAddons("lua/autorun/wiremod_installed_improperly_popup.lua")
		
			-- did server send bad install? if so, blame server
			if WireServerBadInstall then
				hint = [[
The Server's copy of wiremod is not installed properly. Please notify the admin of our tutorial at:
http://www.wiremod.com/forum/wiremod-general-chat/4-wiremod-svn-guide.html
A copy of this link is found in the wiremod install on the server named "How to Install.txt"]]
				
				-- check for client wire, if missing, replace above hint with both blamed
				if WireClientBadInstall then
					hint = [[
The copy of wiremod on the server and your client is not installed properly. Please notify the admin,
and follow our tutorial located at the following web page:
http://www.wiremod.com/forum/wiremod-general-chat/4-wiremod-svn-guide.html
A copy of this link is found in the wiremod install on both the server and your client named "How to Install.txt"]]
				end -- if WireClientBadInstall
			end -- if WireServerBadInstall
			
			if not (WireServerBadInstall or WireClientBadInstall) then
				-- this shouldn't _ever_ run. Put here for freak installs.
				MsgN("SinglePlayer() " .. tostring(SinglePlayer()) .. ", isListenHost " .. tostring(isListenHost) .. ", WireServerBadInstall " .. tostring(WireServerBadInstall) .. ", WireClientBadInstall " .. tostring(WireClientBadInstall))
				hint = [[
SOMEHOW, this section of the script ran. This was put here for the freak cases, so you get this:
  xxxxx       _____
 x     x     /     \
x  O  O x   /  Moo  |
x    D  x  /_______/
x   --- x //
 x     x
  xxxxx
EASTER EGG from TomyLobo & Anticept.
Oh, by the way, make sure you tell us at wiremod.com, because this part of the script should have NEVER been ran.
Post it in the "Help and Support" section, and post a condump too.
Lastly, read the "Ultimate guide to problems in GMod" sticky in that section to create a CLEAN Garrys Mod Install.
]]
			end -- if (weird)
		end -- if dedicated_server_or_listen_server_client
		PrintWBI(hint)
	end -- function clienthint
	clienthint()
end -- if CLIENT
