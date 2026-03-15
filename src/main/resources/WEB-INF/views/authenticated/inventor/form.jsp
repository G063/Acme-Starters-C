<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textarea code="inventor.form.label.bio" path="bio"/>
    <acme:form-textbox code="inventor.form.label.key-words" path="keyWords"/>
    <acme:form-checkbox code="inventor.form.label.licensed" path="licensed"/>
    
    <jstl:if test="${_command == 'create'}">
        <acme:submit code="inventor.form.button.create" action="/authenticated/inventor/create"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'update'}">
        <acme:submit code="inventor.form.button.update" action="/authenticated/inventor/update"/>
    </jstl:if>
</acme:form>