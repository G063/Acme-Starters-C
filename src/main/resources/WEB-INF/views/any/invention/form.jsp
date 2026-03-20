<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:form-textbox code="any.invention.label.ticker" path="ticker" readonly="true"/>
    <acme:form-textbox code="any.invention.label.name" path="name" readonly="true"/>
    <acme:form-money code="any.invention.label.cost" path="cost" readonly="true"/>
    <acme:form-moment code="any.invention.label.startMoment" path="startMoment" readonly="true"/>
    <acme:form-moment code="any.invention.label.endMoment" path="endMoment" readonly="true"/>
    <acme:form-double code="any.invention.label.monthsActive" path="monthsActive" readonly="true"/>
    <acme:form-textarea code="any.invention.label.description" path="description" readonly="true"/>
    
    <hr/>

    <acme:button code="any.invention.button.parts" action="/any/part/list?inventionId=${id}"/>
    <acme:button code="any.invention.button.inventor" action="/any/inventor/show?id=${inventorId}&inventionId=${id}"/>

</acme:form>