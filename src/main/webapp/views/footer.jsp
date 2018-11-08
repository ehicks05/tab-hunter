<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="footer">
    <div class="container">
        <div class="columns">
            <div class="column has-text-right">
                <p>
                    <img src="${pageContext.request.contextPath}/resources/guitar.png" style="width:28px;"/>
                    TAB build <span title="${userSession.systemInfo.gitVersion}">${userSession.systemInfo.version}</span> by HicksTeam

                    <c:if test="${devOrProd eq 'dev'}"><span>🛠</span></c:if>
                    <c:if test="${devOrProd eq 'prod'}"><span>💎️</span></c:if>

                </p>
                <p>
                    <c:if test="${!empty sessionScope.userSession}">
                        <span title="Time from entering the controller to rendering this element.">${userSession.currentTimeMillis - userSession.enteredController} ms</span>
                    </c:if>
                </p>
            </div>
        </div>
    </div>
</footer>

<c:remove var="lastRequestDuration" scope="session" />