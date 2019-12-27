package net.qiujuer.web.italker.push.factory;

import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.utils.Hib;
import net.qiujuer.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

public class UserFactory {
    /**
     * 用户注册
     * 注册的操作需要写入数据库，并返回数据库中的User信息
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String name) {

        User user = new User();
        user.setPhone(account);
        user.setName(name);
        user.setPassword(encodePassword(password));

        // 操作数据库
        Session session = Hib.session();
        session.beginTransaction();

        try{
            session.save(user);
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
        }

        return user;
    }

    private static String encodePassword(String password){
        // 去除首位空格
        password = password.trim();
        // 先进行md5非对称加密
        password = TextUtil.getMD5(password);
        // 再进行base64 对称加密
        return TextUtil.encodeBase64(password);
    }

}
