package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import com.clzy.web.core.dto.JsonResponse;
import com.clzy.web.crud.persistence.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.clzy.web.crud.web.PageController;
import ${package.Mapper}.${table.mapperName};
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
@Api(tags = "${table.comment!}相关的接口")
public class ${table.controllerName} extends PageController<${table.serviceName}, ${table.mapperName}, ${entity}> {
</#if>

<#--    @Autowired
    private ${table.serviceName} ${cfg.service};

    @ApiOperation(value = "自定义分页条件排序获取${table.comment!}信息",notes = "自定义分页条件排序获取${table.comment!}信息")
    @PostMapping("page")
    public JsonResponse list(@RequestBody @ApiParam ${entity} ${cfg.param}, HttpServletRequest request, HttpServletResponse response) {
        Page<${entity}> page = new Page<>(request, response);
        page = ${cfg.service}.findPage(page, ${cfg.param});
        return JsonResponse.success(page);
    }

    @ApiOperation(value = "上传${table.comment!}信息",notes = "上传${table.comment!}信息")
    @PostMapping("save")
    public JsonResponse save(@RequestBody ${entity} ${cfg.param}) {
        ${cfg.service}.save(${cfg.param});
        return JsonResponse.success(true);
    }

    @ApiOperation(value = "根据主键获取${table.comment!}信息",notes = "根据主键获取${table.comment!}信息")
    @GetMapping("get")
    public JsonResponse get(Long id)
    {
    return JsonResponse.success(${cfg.service}.get(id));
    }

    @ApiOperation(value = "删除${table.comment!}信息",notes = "删除${table.comment!}信息")
    @PostMapping("delete")
    public JsonResponse delete(Long id)
    {
        ${cfg.service}.delete(new ${entity}(id));
        return JsonResponse.success(true);
    }-->
}
</#if>
