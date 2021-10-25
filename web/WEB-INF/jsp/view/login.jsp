<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <link href="css/bootstrap.css" rel='stylesheet' type='text/css' />
        <!-- jQuery (Bootstrap's JavaScript plugins) -->
        <script src="js/jquery.min.js"></script>
        <!-- Custom Theme files -->
        <link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
        <!-- Custom Theme files -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="Bike-shop Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
              Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
        <script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
        <!--webfont-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:500,900,100,300,700,400' rel='stylesheet' type='text/css'>
        <!--webfont-->
        <!-- dropdown -->
        <script src="js/jquery.easydropdown.js"></script>
        <link href="css/nav.css" rel="stylesheet" type="text/css" media="all"/>
        <script src="js/scripts.js" type="text/javascript"></script>
        <!--js-->
        <!---- start-smoth-scrolling---->
        <script type="text/javascript" src="js/move-top.js"></script>
        <script type="text/javascript" src="js/easing.js"></script>
        <script type="text/javascript">
            jQuery(document).ready(function ($) {
                $(".scroll").click(function (event) {
                    event.preventDefault();
                    $('html,body').animate({scrollTop: $(this.hash).offset().top}, 900);
                });
            });
        </script>
        <!---- start-smoth-scrolling---->


    </head>
    <body>
        <!--banner-->
        <script src="js/responsiveslides.min.js"></script>
        <script>
                            $(function () {
                                $("#slider").responsiveSlides({
                                    auto: true,
                                    nav: true,
                                    speed: 500,
                                    namespace: "callbacks",
                                    pager: true,
                                });
                            });
        </script>
        <div class="banner-bg banner-bg1">	
            <c:import url="/WEB-INF/jsp/header.jspf"/>	
            <div class="contact">
                <div class="container">
                    <h3>LOGIN</h3>
                    <form action="login" method="POST">
                        <input type="text" name="userID" placeholder="USERNAME" required=""  maxlength="10">
                        <input type="password" name="password" placeholder="PASSWORD" required=""  maxlength="30">			 
                        <input type="submit" value="LOGIN">
                    </form>
                    <p style="color: orangered">${loginError}</p>
                    <a href="<c:url value="login?action=loginByGG"/>">LOGIN WITH GOOGLE</a><br>
                    <a href="<c:url value="signup"/>">SIGN UP</a>
                    
                </div>

            </div>	 
        </div>
        <!--/banner-->
        <!---->
        <c:import url="/WEB-INF/jsp/footer.jspf"/>	
        <!---->

    </body>
</html>

