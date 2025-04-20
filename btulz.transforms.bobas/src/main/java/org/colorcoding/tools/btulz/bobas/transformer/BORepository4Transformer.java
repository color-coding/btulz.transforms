package org.colorcoding.tools.btulz.bobas.transformer;

import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.ICriteria;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.common.OperationResult;
import org.colorcoding.ibas.bobas.data.IDataTable;
import org.colorcoding.ibas.bobas.db.DbTransaction;
import org.colorcoding.ibas.bobas.db.SqlStatement;
import org.colorcoding.ibas.bobas.repository.BORepositoryService;
import org.colorcoding.ibas.bobas.repository.RepositoryException;

/**
 * 变形金刚专用业务仓库
 * 
 * 不会触发审批流程，使用系统用户权限
 * 
 * @author Niuren.Zhu
 *
 */
public class BORepository4Transformer extends BORepositoryService {

	public BORepository4Transformer() {
	}

	public <P extends IBusinessObject> IOperationResult<P> saveData(P bo) {
		return super.save(bo);
	}

	public <P extends IBusinessObject> IOperationResult<P> fetchData(ICriteria criteria, Class<P> boType) {
		return super.fetch(boType, criteria);
	}

	public IOperationResult<IDataTable> queryData(String query) {
		try {
			if (this.getTransaction() instanceof DbTransaction) {
				DbTransaction transaction = (DbTransaction) this.getTransaction();
				SqlStatement sqlStatement = new SqlStatement(query);
				return new OperationResult<IDataTable>().addResultObjects(transaction.fetch(sqlStatement));
			} else {
				throw new RepositoryException("the transaction not support.");
			}
		} catch (Exception e) {
			return new OperationResult<>(e);
		}
	}
}
