<%-- 
    Document   : product
    Created on : Aug 13, 2021, 9:56:10 AM
    Author     : ACER
--%>
<%--@elvariable id="productList" type="java.util.List<lamnv.DTO.ProductDTO>"--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Products</title>
    </head>
    <body>
        <form action="product">
            Input<input type="text" name="searchNameTxt" value="${request.getParameter("searchNameTxt")}" />
            <input type="submit" name="action" value="searchByName" />           
        </form>
        <form action="product">
            Input<input type="text" name="searchCategoryIDTxt" value="${request.getParameter("searchCategoryIDTxt")}" />
            <input type="submit" name="action" value="searchByCategoryID" />
        </form>
            ${searchByCategoryError}<br>
        <a href="<c:url value="product"/>">See all</a>
        <c:if test="${isAdmin}">
            <a href="<c:url value="product?action=getUnable"/>">See unable products</a>
        </c:if>

        <c:if test="${!empty productList}">
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Category</th>
                            <c:if test="${!isAdmin}">
                            <th>Add to cart</th>
                            </c:if>
                            <c:if test="${isAdmin}">
                            <th>Is Enable</th>
                            <th>Change state</th>
                            </c:if>

                    </tr>
                </thead>

                <tbody>  
                    <c:forEach var="pro" items="${productList}">
                        <tr>
                            <td>${pro.productID}</td>
                            <td>${pro.productName}</td>
                            <td>${pro.price}</td>
                            <td>${pro.quantity}</td>
                            <td>${pro.categoryID} - ${categoryMap.get(pro.categoryID)}</td>
                            <td>
                                <c:if test="${!isAdmin && pro.quantity>0}">
                                    <a href="<c:url value="cart?action=add">
                                           <c:param name="proID" value="${pro.productID}"/>
                                           <c:param name="quantity" value="1"/>
                                       </c:url>">
                                        <button>Add</button>
                                    </a>
                                </c:if>
                                <c:if test="${isAdmin}">
                                    ${pro.isEnable}
                                </c:if>
                            </td>
                            <c:if test="${isAdmin}">
                                <td>
                                    <a href="<c:url value="product?action=updateState">
                                           <c:param name="UpdateStateProductID" value="${pro.productID}"/>
                                       </c:url>">
                                        <button>Switch state</button>
                                    </a>
                                </td> 
                            </c:if>
                        </tr>
                    </c:forEach>    
                </tbody>
            </table>
        </c:if>
        ${updateStateError}

        <c:if test="${isAdmin}">
            <h3>Add new product</h3>
            <form action="product" method="POST">
                Cat <input type="text" name="categoryID" value="${param["categoryID"]}" />
                Name <input type="text" name="productName" value="${param["productName"]}" />
                Price <input type="text" name="productPrice" value="${param["productPrice"]}" />
                Quantity <input type="text" name="productQuantity" value="${param["productQuantity"]}" />
                Enable <input type="checkbox" name="isEnable" />
                <input type="submit" name="action" value="addProduct" />
            </form>
            <h4>Add product message</h4>
            <c:forEach items="${addValidationErrors}" var="er">   
                ${er.getMessage()}<br>
            </c:forEach>
            <hr>
            <h3>Update product</h3>
            <h4>Get product by ID message</h4>
            ${getProductByIdError}
            <form action="product">
                Enter product id to edit <input type="text" name="productID" value="${param["editProductID"]}" />
                <input type="submit" name="action" value="getProductByID" />
            </form>
            <form action="product" method="GET">
                ProductID <input type="text" name="_productID" value="${product.productID}" readonly="readonly" />
                Cat <input type="text" name="_categoryID" value="${product.categoryID}" />
                Name <input type="text" name="_productName" value="${product.productName}" />
                Price <input type="text" name="_productPrice" value="${product.price}" />
                Quantity <input type="text" name="_productQuantity" value="${product.quantity}" />
                Enable: <input type="checkbox" name="_isEnable" value="ON" <c:if test="${product.isEnable}">checked</c:if>/> 
                <input type="submit" name="action" value="updateProduct" />
            </form>
            <h4>Update product message</h4>
            <c:forEach items="${updateValidationErrors}" var="er">   
                ${er.getMessage()}<br>
            </c:forEach>
        </c:if>



    </body>
</html>
