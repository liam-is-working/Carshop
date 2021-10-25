<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html>
    <head>
        <title>Cart</title>
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
            <c:import url="/WEB-INF/jsp/header.jspf"/>	 		 
        </div>
        <!--/banner-->
        <div class="cart">
            <div class="container">

                <div class="col-md-9 cart-items">
                    <p style="color: orangered">${addError}</p>
                    <p style="color: orangered">${deleteError}</p>
                    <p style="color: orangered">${deleteProductError}</p>
                    <c:if test="${! empty cart}">
                        <c:forEach var="pro" items="${cart.keySet()}">
                            <div class="cart-header">
                                <div class="cart-sec">
                                    <div class="cart-item cyc">
                                        <img src="images/bik3.jpg"/>
                                    </div>
                                    <div class="cart-item-info">
                                        <h3>${pro.productName}
                                            <span>ID: ${pro.productID}</span>
                                            <span>Category: ${pro.categoryID} </span>
                                        </h3>
                                        <h4><span>PRICE $ </span>${pro.price}</h4>
                                        <p class="qty">Quantity ::${cart.get(pro)}</p>
                                        <a href="<c:url value="cart?action=add">
                                               <c:param name="quantity" value="1"/>
                                               <c:param name="proID" value="${pro.productID}"/>
                                           </c:url>"><button>+</button></a>
                                        <a href="<c:url value="cart?action=delete">
                                               <c:param name="quantity" value="1"/>
                                               <c:param name="proID" value="${pro.productID}"/>
                                           </c:url>"><button>-</button></a>
                                        <a href="<c:url value="cart?action=deleteProduct">
                                               <c:param name="proID" value="${pro.productID}"/>
                                           </c:url>"><button>Remove</button></a>
                                    </div>
                                    <div class="clearfix"></div>					
                                </div>
                            </div>
                        </c:forEach>
                        <a class="continue" href="<c:url value="cart?action=clearAll" />">Clear all</a>
                    </c:if>
                    <c:if test="${empty cart}">
                        <h2>There's no product in cart</h2>
                    </c:if>
                        ${verificationMailMessage}


                </div>

                <div class="col-md-3 cart-total">
                    <a class="continue" href="<c:url value="product" />">Buy more car</a>
                    <div class="price-details">
                        <h3>Price Details</h3>
                        <span>Total</span>
                        <span class="total">${cart.getTotalPrice()}</span>
                        <span>Discount</span>
                        <span class="total">---</span>
                        <span>Delivery Charges</span>
                        <span class="total">---</span>
                        <div class="clearfix"></div>				 
                    </div>	
                    <h4 class="last-price">TOTAL</h4>
                    <span class="total final">${cart.getTotalPrice()}</span>
                    <div class="clearfix"></div>
                    <hr>
                    <form action="order" method="POST">
                        <input type="text" name="email" value="${param["email"]}" placeholder="EMAIL" required="">
                        <input type="text" name="address" value="${param["address"]}" placeholder="ADDRESS" required="">			 
                        <input type="hidden" name="action" value="submitOrder">			 
                        <input class="order" type="submit" value="PLACE ORDER">
                    </form>
                    <p style="color: orangered">${checkOutError}</p>
                    <p style="color: green">${checkOutSuccess}</p>
                    <p style="color: green">${verificationMailMessage}</p>
                    
                </div>
            </div>
        </div>
        <!---->
        <c:import url="/WEB-INF/jsp/footer.jspf"/>
        <!---->

    </body>
</html>

