<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>	
		<link rel="stylesheet" rev="stylesheet" href="../../theme/css/global.css" type="text/css" media="screen" />
		<script src="../../theme/scripts/jquery-1.5.2.min.js" type="text/javascript"></script>
		<script src="../../theme/scripts/menu.js" type="text/javascript"></script>
		<script src="../../theme/scripts/jquery-ui.min.js" type="text/javascript"></script>
		
		<link rel="stylesheet" type="text/css" href="../../theme/css/style.css" />
		<link rel="stylesheet" type="text/css" href="../../theme/css/ui.css" />
		<link rel="stylesheet" type="text/css" href="../../theme/css/jquery-ui.css" />
		<script>	
		
			$(function() {
				$( "button, input:button, input:submit").button();
			});
			
			$(function() {
				$("#regBtn").click(function() { jAlert("clicked"); });
			});
	
		</script>
    </head>
    <body>
        <div id="main">
            <!-- Content -->
            <div id="contentHeadLeft">
				<div id="contentHeadRight">
					<div id="contentHeadCenter"></div>
				</div>
			</div>
            <div id="contentBodyLeft">
                <div id="contentBodyRight">
                    <div id="contentBodyCenter">
                        <div id="content" style="height:300px;">
							
								
								<div id="box" style="margin-left:120px; margin-top:0; width: 585px; height:250px; align:center">
									<div style="width:500px; height:45px; margin-top:15px; margin-left:5px; float:left; align:center">
										<font color="#000000" Size="2" style="margin-left:170px;">You have been successfully logged out..!!</font><br/>
										<font color="#000000" Size="2" style="margin-left:200px;">Click <a href="./login.jsp">here</a> to login again.</font>
									</div>
								</div>
														
						</div>
                    </div>
                    <div class="clear">&nbsp;</div>
                </div>
            </div>
            <div id="contentFootLeft">
				<div id="contentFootRight">
					<div id="contentFootCenter"></div>
				</div>
			</div>
        </div>
    </body>
</html>

