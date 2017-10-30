package com.qwy.demo.qrscandemo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qwy on 17/3/31.
 * 身份证信息实体 腾讯优图
 */
public class IDCardEntity implements Serializable {

    /**
     * 字段	类型	说明
     * errorcode	Int	返回状态值
     * errormsg	String	返回错误消息
     * session_id	String	保留字段，目前不使用
     * name	String	证件姓名
     * name_confidence_all	Array(Int)	证件姓名置信度
     * sex	String	性别
     * sex_confidence_all	Array(Int)	性别置信度
     * nation	String	民族
     * nation_confidence_all	Array(Int)	民族置信度
     * birth	String	出生日期
     * birth_confidence_all	Array(Int)	出生日期置信度
     * address	String	地址
     * address_confidence_all	Array(Int)	地址置信度
     * id	String	身份证号
     * id_confidence_all	Array(Int)	身份证号置信度
     * frontimage	String	OCR识别的身份证正面base64编码照片
     * frontimage_confidence_all	Array(Int)	正面照片置信度
     * watermask_status	Int	水印是否存在(暂时不提供)
     * watermask_confidence_all	Array(Int)	水印置信度
     * valid_date	String	证件的有效期
     * valid_date_confidence_all	Array(Int)	证件的有效期置信度
     * authority	String	发证机关
     * authority_confidence_all	Array(Int)	发证机关置信度
     * backimage	String	OCR识别的证件身份证反面base64编码照片
     * backimage_confidence_all	Array(Int)	反面照片置信度
     * detail_errorcode	Array(Int)	详细的错误原因
     * detail_errormsg	Array(String)	详细的错误原因说明
     */

    private int errorcode;
    private String errormsg;
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String id;
    private String frontimage;
    private List<Integer> name_confidence_all;
    private List<Integer> sex_confidence_all;
    private List<Integer> nation_confidence_all;
    private List<Integer> birth_confidence_all;
    private List<Integer> address_confidence_all;
    private List<Integer> id_confidence_all;
    private List<?> frontimage_confidence_all;
    private List<?> watermask_confidence_all;
    private List<?> valid_date_confidence_all;
    private List<?> authority_confidence_all;
    private List<?> backimage_confidence_all;
    private List<Integer> detail_errorcode;
    private List<String> detail_errormsg;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontimage() {
        return frontimage;
    }

    public void setFrontimage(String frontimage) {
        this.frontimage = frontimage;
    }

    public List<Integer> getName_confidence_all() {
        return name_confidence_all;
    }

    public void setName_confidence_all(List<Integer> name_confidence_all) {
        this.name_confidence_all = name_confidence_all;
    }

    public List<Integer> getSex_confidence_all() {
        return sex_confidence_all;
    }

    public void setSex_confidence_all(List<Integer> sex_confidence_all) {
        this.sex_confidence_all = sex_confidence_all;
    }

    public List<Integer> getNation_confidence_all() {
        return nation_confidence_all;
    }

    public void setNation_confidence_all(List<Integer> nation_confidence_all) {
        this.nation_confidence_all = nation_confidence_all;
    }

    public List<Integer> getBirth_confidence_all() {
        return birth_confidence_all;
    }

    public void setBirth_confidence_all(List<Integer> birth_confidence_all) {
        this.birth_confidence_all = birth_confidence_all;
    }

    public List<Integer> getAddress_confidence_all() {
        return address_confidence_all;
    }

    public void setAddress_confidence_all(List<Integer> address_confidence_all) {
        this.address_confidence_all = address_confidence_all;
    }

    public List<Integer> getId_confidence_all() {
        return id_confidence_all;
    }

    public void setId_confidence_all(List<Integer> id_confidence_all) {
        this.id_confidence_all = id_confidence_all;
    }

    public List<?> getFrontimage_confidence_all() {
        return frontimage_confidence_all;
    }

    public void setFrontimage_confidence_all(List<?> frontimage_confidence_all) {
        this.frontimage_confidence_all = frontimage_confidence_all;
    }

    public List<?> getWatermask_confidence_all() {
        return watermask_confidence_all;
    }

    public void setWatermask_confidence_all(List<?> watermask_confidence_all) {
        this.watermask_confidence_all = watermask_confidence_all;
    }

    public List<?> getValid_date_confidence_all() {
        return valid_date_confidence_all;
    }

    public void setValid_date_confidence_all(List<?> valid_date_confidence_all) {
        this.valid_date_confidence_all = valid_date_confidence_all;
    }

    public List<?> getAuthority_confidence_all() {
        return authority_confidence_all;
    }

    public void setAuthority_confidence_all(List<?> authority_confidence_all) {
        this.authority_confidence_all = authority_confidence_all;
    }

    public List<?> getBackimage_confidence_all() {
        return backimage_confidence_all;
    }

    public void setBackimage_confidence_all(List<?> backimage_confidence_all) {
        this.backimage_confidence_all = backimage_confidence_all;
    }

    public List<Integer> getDetail_errorcode() {
        return detail_errorcode;
    }

    public void setDetail_errorcode(List<Integer> detail_errorcode) {
        this.detail_errorcode = detail_errorcode;
    }

    public List<String> getDetail_errormsg() {
        return detail_errormsg;
    }

    public void setDetail_errormsg(List<String> detail_errormsg) {
        this.detail_errormsg = detail_errormsg;
    }
}
