$(document).ready(function () {

	$('.close').click(function () {
		$('#dialog-box').hide();
		return false;
	});

	$(".view").click(function() {
		$("#dialog-box").show();
	});
	
	// $('.close', top.document).click(function () {
		// $('#dialog-box', top.document).hide();
		// return false;
	// });

	// $(".view").click(function() {
		// $("#dialog-box", top.document).show();
	// });


	$('.sys_close').click(function () {
		$('#sys-box').hide();
		return false;
	});
	$(".system").click(function() {
		$("#sys-box").show();
	});
});


