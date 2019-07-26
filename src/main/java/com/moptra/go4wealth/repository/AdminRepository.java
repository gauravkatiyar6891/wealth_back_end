package com.moptra.go4wealth.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moptra.go4wealth.bean.Blog;
import com.moptra.go4wealth.bean.Seo;
import com.moptra.go4wealth.bean.User;

public interface AdminRepository extends JpaRepository<Blog, Integer>{

	@Query("FROM Blog as b where b.blogId=:blogId")
	Blog getBlogByBlogId(@Param("blogId") int blogId);
	
	@Query("FROM Seo as s Where s.pageName=:pageName")
	Seo fetchScoInfo(@Param("pageName") String pageName);

	@Query("FROM Blog as b where b.blogCategory.blogCategoryId=:categoryId")
	List<Blog> getBlogByCategoryId(@Param("categoryId") int categoryId);
	
	@Query("FROM Blog as b where b.blogCategory.blogCategoryId=:categoryId")
	List<Blog> getBlogByCategoryId(@Param("categoryId") int categoryId,Pageable page);

	@Query("FROM Blog ORDER BY POST_DATE DESC")
	List<Blog> getLatestBlog(Pageable page);

	@Query("FROM User as u WHERE u.userId IN (SELECT ur.id.userId FROM UserRole as ur WHERE ur.id.roleId != 1)")
	List<User> findUserExceptAdmin();

	@Query("From User as u where u.userId in (select ur.id.userId FROM UserRole as ur where ur.id.userId = :userId and ur.id.roleId in (select r.roleId from Role as r where r.roleName = :roleName))")
	User findUserByRole(@Param("userId") int userId,@Param("roleName") String roleName);
	
}
