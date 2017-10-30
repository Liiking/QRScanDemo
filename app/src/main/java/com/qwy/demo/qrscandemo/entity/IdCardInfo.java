package com.qwy.demo.qrscandemo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qwy on 17/3/31.
 * face++ api 识别出来的身份证信息实体
 */
public class IdCardInfo implements Serializable {

    /**
     * image_id : +8KF9s3B0tHKoPgT8+dVzg==
     * request_id : 1490946333,a4d96f52-1887-4655-9540-7b1135d31f8e
     * cards : [{"name":"李明","gender":"男","id_card_number":"440524198701010014","birthday":"1987-01-01","race":"汉","address":"北京市石景山区高新技术园腾讯大楼","type":1,"side":"front"}]
     * time_used : 902
     */

    private String image_id;
    private String request_id;
    private int time_used;
    /**
     * name : 李明
     * gender : 男
     * id_card_number : 440524198701010014
     * birthday : 1987-01-01
     * race : 汉
     * address : 北京市石景山区高新技术园腾讯大楼
     * type : 1
     * side : front
     */

    private List<CardsInfo> cards;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<CardsInfo> getCards() {
        return cards;
    }

    public void setCards(List<CardsInfo> cards) {
        this.cards = cards;
    }

    public static class CardsInfo implements Serializable {
        private String name;
        private String gender;
        private String id_card_number;
        private String birthday;
        private String race;
        private String address;
        private int type;
        private String side;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId_card_number() {
            return id_card_number;
        }

        public void setId_card_number(String id_card_number) {
            this.id_card_number = id_card_number;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRace() {
            return race;
        }

        public void setRace(String race) {
            this.race = race;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }
    }
}
