var months = new Array("January", "February", "March",
"April", "May", "June", "July", "August", "September",
"October", "November", "December");

//@param month that is in a 'hidden' field
function init() {
	var str = document.getElementById("hidden").innerHTML;
	loadTab(str); 
}

//display an error message as an alert box
function errMsg(key, msg){
	alert(msg);
}




//find the correct tab, based on the month
//@param month as a string
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






//function to display the correct tabbed header on index page for a month
//@param string to denote the month
function tab(tab) {

	//first set  all tabs to not active, then set selected tab to active, and display to block

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

	document.getElementById('li_'+tab).setAttribute("class", "active");

	}




