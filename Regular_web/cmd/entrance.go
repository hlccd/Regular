package cmd

/**
路由配置
*/
import (
	"../controller"
	"github.com/gin-gonic/gin"
	"github.com/hlccd/util/jwt"
)

func Entrance() {
	r := gin.Default()
	//用户路由
	user := r.Group("/user")
	{
		user.POST("/register", controller.Register)
		//登录
		user.GET("/login", controller.Login)
		//jw鉴权
		user.Use(jwt.CheckToken())
		user.GET("/message", controller.GainUserMessage)
		user.GET("/search", controller.SearchUsers)
		user.GET("/friend", controller.FriendList)
		//更改用户信息
		change := user.Group("/change")
		{
			//更改用户名
			change.POST("/name", controller.ChangeUserName)
			//更改密码
			change.POST("/passage", controller.ChangePassword)
			//更改手机
			change.POST("/phone", controller.ChangePhone)
			//更改邮箱
			change.POST("/email", controller.ChangeEmail)
		}
	}
	group := r.Group("/group")
	{
		group.Use(jwt.CheckToken())
		group.GET("/my", controller.MyGroupList)
		find := group.Group("/find")
		{
			find.GET("/member", controller.FindMember)
			find.GET("/groups", controller.FindGroups)
		}
		group.POST("/establish", controller.Establish)
		change := group.Group("/change")
		{
			change.POST("/name", controller.ChangeGroupName)
			change.POST("/master", controller.ChangeMaster)
		}
		group.POST("/join", controller.Join)
		group.POST("/kick", controller.Kick)
		setup := group.Group("/setup")
		{
			setup.POST("/administrator", controller.SetupAdministrator)
		}
		cancel := group.Group("/cancel")
		{
			cancel.POST("/administrator", controller.CancelAdministrator)
			cancel.POST("/group", controller.CancelGroup)
		}
	}
	project := r.Group("/project")
	{
		project.Use(jwt.CheckToken())
		find := project.Group("/find")
		{
			find.GET("/habit", controller.FindHabit)
			find.GET("/user_task", controller.FindUserTask)
			find.GET("/group_task", controller.FindGroupTask)
			find.GET("/implement", controller.FindImplement)
		}
		increase := project.Group("/increase")
		{
			increase.POST("/habit", controller.IncreaseHabit)
			increase.POST("/user_task", controller.IncreaseUserTask)
			increase.POST("/group_task", controller.IncreaseGroupTask)
		}
		done := project.Group("/done")
		{
			done.POST("/habit", controller.DoneHabit)
			done.POST("/user_task", controller.DoneUserTask)
			done.POST("/group_task", controller.DoneGroupTask)
			done.POST("/implement", controller.DoneImplement)
		}
	}
	music := r.Group("./music")
	{
		music.GET("/listen", controller.ListenMusic)
		music.GET("/hot", controller.HotMusic)
		music.Use(jwt.CheckToken())
		music.POST("/md5", controller.Md5Check)
		music.POST("/file", controller.UploadMusic)
		music.GET("/list", controller.MusicList)
	}
	r.Run(":2975")
}
