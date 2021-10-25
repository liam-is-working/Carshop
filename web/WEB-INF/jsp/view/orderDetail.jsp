<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html>
    <head>
        <title>Order details</title>
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
        <script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
        <!--webfont-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:500,900,100,300,700,400' rel='stylesheet' type='text/css'>
        <!--webfont-->
        <!-- dropdown -->
        <script src="js/jquery.easydropdown.js"></script>
        <link href="css/nav.css" rel="stylesheet" type="text/css" media="all"/>
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
            <div class="container">
                <c:import url="/WEB-INF/jsp/header.jspf"/>	
            </div> 				 
        </div>
        <!--/banner-->
        <div class="cart">
            <div class="container">
                <div class="col-md-9 cart-items">
                    <h2>ORDER ID: ${param.get("orderID")}</h2>
                    <c:forEach items="${productList}" var="pro">
                        <div class="cart-header">
                        <div class="cart-sec">
                            <div class="cart-item cyc">
                                <img src="images/bik3.jpg"/>
                            </div>
                            <div class="cart-item-info">
                                <h3>${pro.productName}
                                    <span>ID: ${pro.productID}</span>
                                    <span>Category: ${pro.categoryID}</span></h3>
                                <h4><span>PRICE $ </span>${pro.price}</h4>
                                <p class="qty">Quantity ::${pro.quantity}</p>
                                
                            </div>
                            <div class="clearfix"></div>					
                        </div>
                    </div>
                    </c:forEach>
                    
                </div>

                <div class="col-md-3 cart-total">
                    <a class="continue" href="<c:url value="product"/>">Buy more car</a>
                    <div class="price-details">
                        <h3>Price Details</h3>
                        <span>Total</span>
                        <span class="total">${totalPrice}</span>
                        <span>Discount</span>
                        <span class="total">---</span>
                        <span>Delivery Charges</span>
                        <span class="total">---</span>
                        <div class="clearfix"></div>				 
                    </div>	
                    <h4 class="last-price">TOTAL</h4>
                    <span class="total final">${totalPrice}</span>
                    <br><br><br><br><br><br><br><br><br><br><br><br><br>
                    <div class="clearfix"></div>

                </div>
            </div>

        </div>
        <div class="clearfix"></div>	
        <!---->
        <c:import url="/WEB-INF/jsp/footer.jspf"/>
        <!---->

    </body>
</html>

