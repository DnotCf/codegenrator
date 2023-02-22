<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
    <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.name},
</#list>
        ${table.fieldNames}
    </sql>
</#if>

    <sql id="sqlColums">
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
                a.${field.name} AS "${field.propertyName}",
            </#if>
        </#list>
        <#list table.commonFields as field><#--生成公共字段 -->
            a.${field.name} AS "${field.propertyName}",
        </#list>
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
                a.${field.name} AS "${field.propertyName}"<#if field_has_next>,</#if>
            </#if>
        </#list>
    </sql>

    <sql id="sqlJoins">
    </sql>

    <sql id="propertyFields">
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
                <#noparse>#{</#noparse>${field.propertyName}},
            </#if>
        </#list>
        <#list table.commonFields as field><#--生成公共字段 -->
            <#noparse>#{</#noparse>${field.propertyName}},
        </#list>
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
                <#noparse>#{</#noparse>${field.propertyName}}<#if field_has_next>,</#if>
            </#if>
        </#list>
    </sql>

    <insert id="insert">
        INSERT INTO ${table.name?upper_case}(
            ${table.fieldNames}
        ) VALUES (
            <include refid="propertyFields"/>
        )
    </insert>

    <update id="update">
        UPDATE ${table.name?upper_case}
        <trim prefix="SET" suffixOverrides=",">
        <#list table.commonFields as field><#--生成公共字段 -->
            <if test="${field.propertyName} !=null">
                ${field.name} = <#noparse>#{</#noparse>${field.propertyName}},
            </if>
        </#list>
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
                <if test="${field.propertyName} !=null">
                    ${field.name} = <#noparse>#{</#noparse>${field.propertyName}}<#if field_has_next>,</#if>
                </if>
            </#if>
        </#list>
        </trim>
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
        WHERE  ${field.name} = <#noparse>#{</#noparse>${field.propertyName}}
            </#if>
        </#list>
    </update>

    <select id="get" resultType="${entity}">
        SELECT
        <include refid="sqlColums"/>
        FROM ${table.name?upper_case} a
        <include refid="sqlJoins"/>
        <#list table.fields as field>
              <#if field.keyFlag><#--生成主键排在第一位-->
        WHERE  a.${field.name} = <#noparse>#{</#noparse>${field.propertyName}}
              </#if>
        </#list>
        AND a.DELETED = 0
    </select>
    
    <select id="findList" resultType="${entity}">
        SELECT
        <include refid="sqlColums"/>
        FROM ${table.name?upper_case} a
        <include refid="sqlJoins"/>
        <where>
            <#list table.fields as field>
              <#if !field.keyFlag><#--生成普通字段 -->
                <if test="${field.propertyName} !=null">
                    and a.${field.name} = <#noparse>#{</#noparse>${field.propertyName}}
                </if>
              </#if>
            </#list>
            <choose>
                <when test='id != null and @com.clzy.geo.core.utils.StringUtils@contains(id, ",")'>
                    <bind name="idList" value='@com.clzy.geo.core.utils.StringUtils@split(id, ",")'/>
                    AND a.ID IN
                    <foreach collection="idList" item="item" open="(" close=")" separator=",">
                        <#noparse>#{item}</#noparse>
                    </foreach>
                </when>
                <when test="id != null and id != ''">
                    AND a.ID = <#noparse>#{id}</#noparse>
                </when>
                <otherwise>
                </otherwise>
            </choose>
        </where>
        <choose>
            <when test="page !=null and page.orderBy != null and page.orderBy != ''">
                ORDER BY <#noparse>${page.orderBy}</#noparse>
            </when>
            <otherwise>
                ORDER BY a.UPDATE_DATE DESC
            </otherwise>
        </choose>
    </select>

    <update id="delete">
        UPDATE ${table.name?upper_case}
        SET DELETED = 1
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
        WHERE  ${field.name} = <#noparse>#{</#noparse>${field.propertyName}}
            </#if>
        </#list>
    </update>

    <insert id="insertList">
        INSERT INTO ${table.name?upper_case}(
            ${table.fieldNames}
        ) VALUES
        <foreach collection="list" item="item" separator=",">
            (
                <#list table.fields as field>
                    <#if field.keyFlag><#--生成主键排在第一位-->
                        <#noparse>#{</#noparse>item.${field.propertyName}},
                    </#if>
                </#list>
                <#list table.commonFields as field><#--生成公共字段 -->
                    <#noparse>#{</#noparse>item.${field.propertyName}},
                </#list>
                <#list table.fields as field>
                <#if !field.keyFlag><#--生成普通字段 -->
                    <#noparse>#{</#noparse>item.${field.propertyName}}<#if field_has_next>,</#if>
                </#if>
                </#list>
            )
        </foreach>

    </insert>

</mapper>
