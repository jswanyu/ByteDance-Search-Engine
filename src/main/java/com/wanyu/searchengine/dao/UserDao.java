package com.wanyu.searchengine.dao;

import com.wanyu.searchengine.entity.TreeNode;
import com.wanyu.searchengine.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname: UserDao
 * @author: wanyu
 * @Date: 2022/7/24 21:38
 */
@Mapper
public interface UserDao {

    User queryOne(String username);

    int insertOne(User user);

    // 返回最高级结点的id
    List<Integer> queryAllNodesByUsername(@Param("username") String username);

    // 返回所有
    List<TreeNode> queryAll(@Param("username") String username);

    boolean updateTreeNodeNameById(@Param("newName") String newName, @Param("id") String id);

    boolean deleteTreeNodeById(@Param("id") String id);

    int getTreeNodeMaxId();

    String getUserIdByUsername(@Param("username") String username);

    boolean addTreeNode(@Param("id") String id, @Param("pid") String pid, @Param("name") String name,
                        @Param("isLeaf") boolean isLeaf, @Param("userId") String userId, @Param("url") String url);
}
