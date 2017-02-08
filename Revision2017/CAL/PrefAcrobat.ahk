IfWinExist, Adobe Acrobat Professional,,,
{

	WinActivate, Adobe Acrobat Professional,,,
	WinMove, 0, 0
	Send, ^k
	Sleep, 100

	MouseMove, 45, 338
	Click

	Sleep, 100
	MouseMove, 334, 69
	Click
	Sleep, 100
	MouseMove, 326, 155
	Click

	Sleep, 100
	MouseMove, 438, 459
	Click
	Sleep, 100
	MouseMove, 294, 574
	Click

	Sleep, 100
	MouseMove, 587, 542
	Click
	
	Sleep, 300
	Send, ^+r

	Sleep, 100
	MouseMove, 60, 152
	Click
	
	Sleep, 500
	Send, {Enter}

	Sleep, 100
	Send, ^z
	
}


ExitApp	