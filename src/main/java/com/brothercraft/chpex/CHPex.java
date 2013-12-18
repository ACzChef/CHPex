package com.brothercraft.chpex;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.annotations.startup;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CHPex {

	@startup
	public static void onEnable(){
	    try {
		Static.checkPlugin("PermissionsEx", Target.UNKNOWN);
	    } catch (Exception e) {
		System.out.println("[CommandHelper] CHPex Could not find PermissionsEx please make sure you have it installed.");
	    }
		System.out.println("[CommandHelper] CHPex Initialized - ACzChef");
	}

	@api
	public static class pex_get_group_users extends AbstractFunction {
		public String getName() {
			return "pex_get_group_users";

		}
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("PermissionsEx", t);
			PermissionManager pex = PermissionsEx.getPermissionManager();
			String g;
			CArray cusers = new CArray(t);
			if (args[0] instanceof CString) {
				g = args[0].val();
			} else {
				return new CVoid(t);
			}
			PermissionUser[] users = pex.getUsers(g);

			for (PermissionUser user : users) {
				cusers.push(new CString(user.getName(), t));
			}
			return cusers;
		}

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.InvalidPluginException};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public String docs() {
			return "array {group} returns an array of all users in a group.";
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
	}
	
	@api
	public static class pex_get_groups extends AbstractFunction {

		public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
			Static.checkPlugin("PermissionsEx", t);
			PermissionManager pex = PermissionsEx.getPermissionManager();
			CArray ret = CArray.GetAssociativeArray(t);
			for(PermissionGroup g : pex.getGroups()) {
				CArray details = CArray.GetAssociativeArray(t);
				details.set("rank", new CInt(g.getRank(), t), t);
				details.set("ladder", new CString(g.getRankLadder(), t), t);
				details.set("prefix", new CString(g.getOwnPrefix(), t), t);
				details.set("suffix", new CString(g.getOwnSuffix(), t), t);
				ret.set(g.getName(), details, t);
			}
			return ret;
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return false;
		}

		public ExceptionType[] thrown() {
			return new ExceptionType[]{ExceptionType.InvalidPluginException};
		}

		public String docs() {
			return "array {} Returns all groups with their rank, rank-ladder, prefix, and suffix.";
		}

		public String getName() {
			return "pex_get_groups";
		}

		public Integer[] numArgs() {
			return new Integer[]{0};
		}

		public CHVersion since() {
			return CHVersion.V3_3_1;
		}
		
	}
	
	public static class pex_get_user_info extends AbstractFunction {

	public ExceptionType[] thrown() {
	    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public boolean isRestricted() {
	    return true;
	}

	public Boolean runAsync() {
	    return false;
	}

	public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
	    Static.checkPlugin("PermissionsEx", t);
	    PermissionManager pex = PermissionsEx.getPermissionManager();
	    CArray ret = CArray.GetAssociativeArray(t);
	    PermissionUser pexuser;
	    if (args.length == 1) {
		pexuser = pex.getUser(args[0].val());
	    } else {
		pexuser = pex.getUser(environment.getEnv(CommandHelperEnvironment.class).GetPlayer().getName());
	    }
	    ret.set("prefix", new CString(pexuser.getOwnPrefix(), t), t);
	    ret.set("suffix", new CString(pexuser.getOwnSuffix(), t), t);
	    return ret;
	}

	public String getName() {
	    return "pex_get_user_info";
	}

	public Integer[] numArgs() {
	    return new Integer[] {0, 1};
	}

	public String docs() {
	    return "array {[player]} Returns player info. prefix, and suffix at the moment.";
	}

	public Version since() {
	    return CHVersion.V3_3_1;
	}
	    
	}
}