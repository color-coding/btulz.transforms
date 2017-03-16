package org.colorcoding.tools.btulz.bobas.commands;

import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.OperationResult;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.ibas.bobas.repository.BORepositoryLogicService;

/**
 * 审批流程专用业务仓库
 * 
 * @author Niuren.Zhu
 *
 */
class BORepository4Init extends BORepositoryLogicService {

	public BORepository4Init() {
		this.setUseCache(false); // 不使用缓存
		this.setCheckApprovalProcess(false);// 不使用审批流程
		// this.setCurrentUser(User.SYSTEM_USER);// 使用系统用户
	}

	public boolean openRepository() throws RepositoryException {
		return super.openRepository();
	}

	public void closeRepository() throws RepositoryException {
		super.closeRepository();
	}

	public boolean beginTransaction() throws RepositoryException {
		return super.beginTransaction();
	}

	public void rollbackTransaction() throws RepositoryException {
		super.rollbackTransaction();
	}

	public void commitTransaction() throws RepositoryException {
		super.commitTransaction();
	}

	public <P extends IBusinessObject> OperationResult<P> save(P bo) {
		String token = this.getCurrentUser().getToken();
		return super.save(bo, token);
	}
}
