<!--
Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<!DOCTYPE html>
<html>
    <head>
        <title>Products</title>
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
        <div class="bikes">		 
            <div class="mountain-sec">
                <form action="product">
                    <input type="text" name="searchNameTxt" placeholder="Name" required="">
                    <input type="hidden" name="action" value="searchByName">
                    <input type="submit" value="SEARCH">        
                </form>
                <c:if test="${empty productList}">
                    <h2>NO AVAILABLE CARS</h2>
                    <br><br><br><br><br><br><br><br><br><br>
                </c:if>
                <c:if test="${! empty productList}">
                    <h2>AVAILABLE CARS</h2>
                    <c:forEach items="${productList}" var="pro">
                    <c:url var="addURL" value="cart?action=add">
                        <c:param name="proID" value="${pro.productID}"/>
                        <c:param name="quantity" value="1"/>
                    </c:url>
                    <a href="${addURL}"><div class="bike">				 
                            <img src="images/bik3.jpg" alt=""/>
                            <div class="bike-cost">
                                <div class="bike-mdl">
                                    <h4>${pro.productName}<span>ID:${pro.productID}</span>
                                        <span>Category:${categoryMap.get(pro.categoryID)}</span>
                                        <span>Price:${pro.price}</span>
                                        <span>Quantity:${pro.quantity}</span></h4>
                                </div>
                                <div class="bike-cart">						 
                                    <a class="buy" href="${addURL}">ADD TO CART</a>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div></a>	
                    </c:forEach>
                </c:if>
                

                <div class="clearfix"></div>
            </div>		 
        </div>
    </div>
    <!---->
    <c:import url="/WEB-INF/jsp/footer.jspf"/>
    <!---->

</body>
</html>

