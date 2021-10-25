<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html>

    <head>
        <title>Order</title>
        <link href="css/bootstrap.css" rel='stylesheet' type='text/css' />
        <!-- jQuery (Bootstrap's JavaScript plugins) -->
        <script src="js/jquery.min.js"></script>
        <!-- Custom Theme files -->
        <link href="css/form.css" rel="stylesheet" type="text/css" media="all" />
        <link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
        <!-- Custom Theme files -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="bike Shop Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
              Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
        <script
        type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
        <!--webfont-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:500,900,100,300,700,400' rel='stylesheet' type='text/css'>
        <!--webfont-->
        <!-- dropdown -->
        <script src="js/jquery.easydropdown.js"></script>
        <link href="css/nav.css" rel="stylesheet" type="text/css" media="all" />
        <script src="js/scripts.js" type="text/javascript"></script>
        <!--js-->

    </head>

    <body>
        <!--banner-->
        <script src="js/responsiveslides.min.js"></script>
        <script>
            $(function () {
                $("#slider").responsiveSlides({
                    auto: false,
                    nav: true,
                    speed: 500,
                    namespace: "callbacks",
                    pager: true,
                });
            });
        </script>
        <div class="banner-bg banner-sec">
            <c:import url="/WEB-INF/jsp/header.jspf"/>	
        </div>
        <!--/banner-->
        <div class="cart">
            <div class="container">
                <div class="cart-items">
                    <c:if test="${! empty orderList}">
                        <h2>ORDERS</h2>
                        <c:forEach var="order" items="${orderList}" >
                            <div class="cart-header">
                                <div class="cart-sec">
                                    <h3>Order ID: ${order.orderID}</h3>
                                    <div class="cart-item-info">
                                        <c:if test="${user.roleID==2}">
                                            <h3>Owner: ${order.userID}</h3>
                                        </c:if>
                                        <h3>Order time: ${order.orderDate}</h3>
                                        <h3>Verify: ${order.isVerified}</h3>
                                        <a href="<c:url value="order?action=orderDetail">
                                               <c:param name="orderID" value="${order.orderID}"/>
                                           </c:url>"><button>Details</button></a>
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
                <c:if test="${empty orderList}">
                    <h3>No order has been made</h3>
                </c:if>
                <c:if test="${empty orderList || orderList.size()<5}">
                    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
                </c:if>

                <div class="clearfix"></div>
            </div>

            <!---->
            <c:import url="/WEB-INF/jsp/footer.jspf"/>
            <!---->

    </body>

</html>