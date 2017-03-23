package org.colorcoding.tools.btulz.bobas.transformers;

import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.OperationResult;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.ibas.bobas.organization.IOrganizationManager;
import org.colorcoding.ibas.bobas.organization.IUser;
import org.colorcoding.ibas.bobas.organization.OrganizationFactory;
import org.colorcoding.ibas.bobas.repository.BORepositoryLogicService;

/**
 * 变形金刚专用业务仓库
 * 
 * 不会触发审批流程，使用系统用户权限
 * 
 * @author Niuren.Zhu
 *
 */
public class BORepository4Transformer extends BORepositoryLogicService implements IBORepository4Transformer {

	public BORepository4Transformer() {
		this.setUseCache(false); // 不使用缓存
		this.setCheckApprovalProcess(false);// 不使用审批流程
		// 使用系统用户
		IOrganizationManager orgManager = OrganizationFactory.create().createManager();
		IUser user = orgManager.getUser(IOrganizationManager.SYSTEM_USER_SIGN);
		if (user == null) {
			throw new RuntimeException("not found system user.");
		}
		this.setCurrentUser(user);
	}

	@Override
	public boolean openRepository() throws RepositoryException {
		return super.openRepository();
	}

	@Override
	public void closeRepository() throws RepositoryException {
		super.closeRepository();
	}

	@Override
	public boolean beginTransaction() throws RepositoryException {
		return super.beginTransaction();
	}

	@Override
	public void rollbackTransaction() throws RepositoryException {
		super.rollbackTransaction();
	}

	@Override
	public void commitTransaction() throws RepositoryException {
		super.commitTransaction();
	}

	@Override
	public <P extends IBusinessObject> OperationResult<P> save(P bo) {
		String token = this.getCurrentUser().getToken();
		return super.save(bo, token);
	}
}
