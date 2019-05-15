package coms.example.administrator.smosmap.mode;

import java.util.List;

public class MapJianWei {
    private String templateName;
    private String fooViewAlpha;
    private String description;
    private String compositeMode;
    //    private String keyTemplate;
//    private AKeyTemplate aKeyTemplate;
    private AKeyTemplate keyTemplate;

    public AKeyTemplate getKeyTemplate() {
        return keyTemplate;
    }

    public void setKeyTemplate(AKeyTemplate keyTemplate) {
        this.keyTemplate = keyTemplate;
    }

    //    public AKeyTemplate getaKeyTemplate() {
//        return aKeyTemplate;
//    }
//
//    public void setaKeyTemplate(AKeyTemplate aKeyTemplate) {
//        this.aKeyTemplate = aKeyTemplate;
//    }


    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFooViewAlpha() {
        return fooViewAlpha;
    }

    public void setFooViewAlpha(String fooViewAlpha) {
        this.fooViewAlpha = fooViewAlpha;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompositeMode() {
        return compositeMode;
    }

    public void setCompositeMode(String compositeMode) {
        this.compositeMode = compositeMode;
    }

//    public String getKeyTemplate() {
//        return keyTemplate;
//    }
//
//    public void setKeyTemplate(String keyTemplate) {
//        this.keyTemplate = keyTemplate;
//    }
}
