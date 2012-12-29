var months = new Array("January", "February", "March",
"April", "May", "June", "July", "August", "September",
"October", "November", "December");

function init() {
	var str = $("#hidden").text();
	loadTab(str); 
}


function errMsg(key, msg){

	alert(msg);
}





function loadTab(str){
	
	var t;
	
	if (str=="January")
		t="tab1";	   
	if (str=="February")
		t="tab2";	   
	if (str=="March")
		t="tab3";
	if (str=="April")
		t="tab4";
	if (str=="May")
		t="tab5";	   
	if (str=="June")
		t="tab6";	   
	if (str=="July")
		t="tab7";
	if (str=="August")
		t="tab8";
	if (str=="September")
		t="tab9";	   
	if (str=="October")
		t="tab10";	   
	if (str=="November")
		t="tab11";
	if (str=="December")
		t="tab12";		
	
   		tab(t);
}






//function for thetabbed section on index page for each month, (shows and hides certain elements) 
function tab(tab) {

	//first set all displays to none, and all tabs to not active, then set selected tab to active, and display to block
	/*
	document.getElementById('tab1').style.display = 'none';
	document.getElementById('tab2').style.display = 'none';
	document.getElementById('tab3').style.display = 'none';
	document.getElementById('tab4').style.display = 'none';
	document.getElementById('tab5').style.display = 'none';
	document.getElementById('tab6').style.display = 'none';
	document.getElementById('tab7').style.display = 'none';
	document.getElementById('tab8').style.display = 'none';
	document.getElementById('tab9').style.display = 'none';
	document.getElementById('tab10').style.display = 'none';
	document.getElementById('tab11').style.display = 'none';
	document.getElementById('tab12').style.display = 'none';
	*/

	document.getElementById('li_tab1').setAttribute("class","");
	document.getElementById('li_tab2').setAttribute("class","");
	document.getElementById('li_tab3').setAttribute("class","");
	document.getElementById('li_tab4').setAttribute("class","");
	document.getElementById('li_tab5').setAttribute("class","");
	document.getElementById('li_tab6').setAttribute("class","");
	document.getElementById('li_tab7').setAttribute("class","");
	document.getElementById('li_tab8').setAttribute("class","");
	document.getElementById('li_tab9').setAttribute("class","");
	document.getElementById('li_tab10').setAttribute("class","");
	document.getElementById('li_tab11').setAttribute("class","");
	document.getElementById('li_tab12').setAttribute("class","");
	var list = ('li_'+tab);

	//document.getElementById(tab).style.display = 'block';
	document.getElementById('li_'+tab).setAttribute("class", "active");

	}




